package ru.lopata.madDiary.featureReminders.presentation.viewTask

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.databinding.FragmentViewTaskBinding
import ru.lopata.madDiary.featureReminders.domain.model.states.AudioItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.FileItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.ImageItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.VideoItemState
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.AttachmentAdapter
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.OnAttachmentDialogListener
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.AudioPreviewDialog
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.ImagePreviewDialog
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.VideoPreviewDialog
import java.sql.Date

@AndroidEntryPoint
class ViewTaskFragment : Fragment(), OnAttachmentDialogListener {

    private var _binding: FragmentViewTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (requireActivity().isDarkTheme()) {
            requireActivity().setNavigationColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.onyx
                )
            )
        }

        _binding = FragmentViewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attachmentAdapter = AttachmentAdapter(this)

        binding.apply {
            viewTaskBackButton.setOnClickListener {
                view.findNavController().navigateUp()
            }
            viewTaskEditButton.setOnClickListener {
                viewModel.editEvent()
            }
            viewTaskDeleteButton.setOnClickListener {
                viewModel.deleteEvent()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    UiEvent.Delete -> {
                        val action = ViewTaskFragmentDirections
                            .actionViewTaskFragmentToBottomReminders(
                                NavigationEvent.Delete(
                                    bundleOf("event" to viewModel.currentEvent.value.toEventRepeatNotificationAttachment())
                                )
                            )

                        view.findNavController().navigate(action)
                    }
                    is UiEvent.Edit -> {
                        view.findNavController().navigate(
                            R.id.createAndEditTaskFragment, event.passObject, NavOptions.Builder()
                                .setPopUpTo(R.id.viewTaskFragment, true)
                                .build()
                        )
                    }
                    else -> {}
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                attachmentAdapter.submitList(event.attachments)
                binding.apply {
                    if (event.chapters > 1)
                        viewTaskTitle.text = "${event.title}(${event.chapter}/${event.chapters})"
                    else
                        viewTaskTitle.text = event.title
                    if (event.allDay) {
                        viewTaskStartDateTime.visibility = View.GONE
                        viewTaskStartDateAndTimeDivider.visibility = View.GONE
                    }
                    viewTaskStartDateDate.text = event.startDateTime.time.toDate()
                    viewTaskStartDateTime.text = event.startDateTime.time.toTimeZone()
                    viewTaskNote.text = event.note
                    if (event.repeatEnd != Date(0)) {
                        viewTaskRepeat.text = resources.getString(event.repeatTitle) +
                                getString(R.string.until) +
                                event.repeatEnd.time.toDate()
                    } else {
                        viewTaskRepeat.text = resources.getString(event.repeatTitle)
                    }

                    var titleString = ""
                    event.notificationTitle.forEach { title ->
                        titleString += if (event.notificationTitle.size == 1)
                            getString(title)
                        else
                            getString(title) + "; "
                    }

                    viewTaskNotification.text = titleString

                    if (event.attachments.isNotEmpty())
                        viewTaskAttachmentRoot.visibility = View.VISIBLE
                    else
                        viewTaskAttachmentRoot.visibility = View.GONE

                    viewTaskCb.isChecked = event.completed
                    viewTaskCb.backgroundTintList = ColorStateList.valueOf(event.color)

                    viewTaskCb.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.updateCheckState(isChecked)
                    }

                    if (event.cover != Uri.EMPTY) {
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(space.id, View.VISIBLE)
                            constraintSet?.applyTo(motionBase)
                        }
                        Glide
                            .with(viewTaskCover.context)
                            .load(event.cover)
                            .into(viewTaskCover)
                    } else {
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(space.id, View.GONE)
                            constraintSet?.applyTo(motionBase)
                        }
                        viewTaskCover.setImageDrawable(null)
                    }
                }
            }
        }
        binding.apply {
            viewTaskAttachmentContentRoot.apply {
                adapter = attachmentAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                addItemDecoration(HorizontalListsItemDecoration(10, 10))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPermissionButtonClick() {

    }

    override fun onInternalButtonClick() {

    }

    override fun onCoverChosen(uri: Uri) {

    }

    override fun onImagesChosen(items: List<ImageItemState>) {

    }

    override fun onImageChosen(item: ImageItemState, state: Boolean) {

    }

    override fun onVideosChosen(items: List<VideoItemState>) {

    }

    override fun onVideoChosen(item: VideoItemState, state: Boolean) {

    }

    override fun onAudioChosen(item: AudioItemState) {

    }

    override fun onFileChosen(item: FileItemState) {

    }

    override fun onImageDialogShow(item: ImageItemState, isChosen: Boolean) {
        val imageDialog = ImagePreviewDialog(item, false, isChosen, this)
        imageDialog.show(
            requireActivity().supportFragmentManager, "ImagePreviewDialog"
        )
    }

    override fun onVideoDialogShow(item: VideoItemState, isChosen: Boolean) {
        val videoDialog = VideoPreviewDialog(item, false,isChosen, this)
        videoDialog.show(
            requireActivity().supportFragmentManager, "VideoPreviewDialog"
        )
    }

    override fun onAudioDialogShow(item: AudioItemState, isChosen: Boolean) {
        val audioDialog = AudioPreviewDialog(item,false, isChosen, this)
        audioDialog.show(
            requireActivity().supportFragmentManager, "AudioPreviewDialog"
        )
    }
}