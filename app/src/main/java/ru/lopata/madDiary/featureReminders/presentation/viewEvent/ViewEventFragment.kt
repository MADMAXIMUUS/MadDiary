package ru.lopata.madDiary.featureReminders.presentation.viewEvent

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.*
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
import ru.lopata.madDiary.databinding.FragmentViewEventBinding
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
class ViewEventFragment : Fragment(), OnAttachmentDialogListener {

    private var _binding: FragmentViewEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attachmentAdapter = AttachmentAdapter()

        binding.apply {
            viewEventBackButton.setOnClickListener {
                view.findNavController().navigateUp()
            }
            viewEventEditButton.setOnClickListener {
                viewModel.editEvent()
            }
            viewEventDeleteButton.setOnClickListener {
                viewModel.deleteEvent()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    UiEvent.Delete -> {
                        val action = ViewEventFragmentDirections
                            .actionViewEventFragmentToBottomReminders(
                                NavigationEvent.Delete(
                                    bundleOf("event" to viewModel.currentEvent.value.toEventRepeatNotificationAttachment())
                                )
                            )

                        view.findNavController().navigate(action)
                    }
                    is UiEvent.Edit -> {
                        view.findNavController().navigate(
                            R.id.createAndEditEventFragment, event.passObject, NavOptions.Builder()
                                .setPopUpTo(R.id.viewEventFragment, true)
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
                        viewEventTitle.text = "${event.title}(${event.chapter}/${event.chapters})"
                    else
                        viewEventTitle.text = event.title
                    if (event.allDay) {
                        viewEventStartDateTime.visibility = View.GONE
                        viewEventStartDateAndTimeDivider.visibility = View.GONE
                        viewEventEndDateTime.visibility = View.GONE
                        viewEventEndDateAndTimeDivider.visibility = View.GONE
                    }
                    viewEventStartDateDate.text = event.startDateTime.time.toDate()
                    viewEventStartDateTime.text = event.startDateTime.time.toTimeZone()
                    viewEventEndDateDate.text = event.endDateTime.time.toDate()
                    viewEventEndDateTime.text = event.endDateTime.time.toTimeZone()
                    viewEventNote.text = event.note
                    if (event.repeatEnd != Date(0)) {
                        viewEventRepeat.text = resources.getString(event.repeatTitle) +
                                getString(R.string.until) +
                                event.repeatEnd.time.toDate()
                    } else {
                        viewEventRepeat.text = resources.getString(event.repeatTitle)
                    }

                    var titleString = ""
                    event.notificationTitle.forEach { title ->
                        titleString += if (event.notificationTitle.size == 1)
                            getString(title)
                        else
                            getString(title) + "; "
                    }

                    viewEventNotification.text = titleString

                    if (event.attachments.isNotEmpty())
                        viewEventAttachmentRoot.visibility = View.VISIBLE
                    else
                        viewEventAttachmentRoot.visibility = View.GONE
                    viewEventColor.setCardBackgroundColor(event.color)
                    if (event.cover != Uri.EMPTY) {
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(space.id, View.VISIBLE)
                            constraintSet?.applyTo(motionBase)
                        }
                        Glide
                            .with(viewEventCover.context)
                            .load(event.cover)
                            .into(viewEventCover)
                    } else {
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(space.id, View.GONE)
                            constraintSet?.applyTo(motionBase)
                        }
                        viewEventCover.setImageDrawable(null)
                    }
                }
            }
        }
        binding.apply {
            viewEventAttachmentContentRoot.apply {
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
        val videoDialog = VideoPreviewDialog(item, false, isChosen, this)
        videoDialog.show(
            requireActivity().supportFragmentManager, "VideoPreviewDialog"
        )
    }

    override fun onAudioDialogShow(item: AudioItemState, isChosen: Boolean) {
        val audioDialog = AudioPreviewDialog(item, false, isChosen, this)
        audioDialog.show(
            requireActivity().supportFragmentManager, "AudioPreviewDialog"
        )
    }
}