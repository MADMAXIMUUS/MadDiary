package ru.lopata.madDiary.featureReminders.presentation.viewEvent

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.databinding.FragmentViewEventBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.AttachmentAdapter
import java.io.File

@AndroidEntryPoint
class ViewEventFragment : Fragment() {

    private var _binding: FragmentViewEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
                    is UiEvent.ShowSnackBar -> {
                        Snackbar.make(
                            view,
                            event.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    UiEvent.Delete -> {
                        viewModel.currentEvent.value.attachments.forEach { attachment ->
                            Uri.parse(attachment.uri).path?.let { File(it).delete() }
                        }
                        view.findNavController().navigateUp()
                    }
                    is UiEvent.Edit -> {
                        view.findNavController().navigate(
                            R.id.createReminderFragment, event.passObject, NavOptions.Builder()
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
                    if (event.chapters == 1)
                        viewEventTitle.text = event.title
                    else
                        viewEventTitle.text = "${event.title}(${event.chapter}/${event.chapters})"
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
                    viewEventRepeat.text = resources.getString(event.repeat) + " " +
                            event.repeatEnd.time.toDateTime()
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
}