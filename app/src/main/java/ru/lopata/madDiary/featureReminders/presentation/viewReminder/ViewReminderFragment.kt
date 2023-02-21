package ru.lopata.madDiary.featureReminders.presentation.viewReminder

import android.annotation.SuppressLint
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
import ru.lopata.madDiary.databinding.FragmentViewReminderBinding
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.AttachmentAdapter
import java.sql.Date

@AndroidEntryPoint
class ViewReminderFragment : Fragment() {

    private var _binding: FragmentViewReminderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewReminderViewModel by viewModels()

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

        _binding = FragmentViewReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attachmentAdapter = AttachmentAdapter()

        binding.apply {
            viewReminderBackButton.setOnClickListener {
                view.findNavController().navigateUp()
            }
            viewReminderEditButton.setOnClickListener {
                viewModel.editEvent()
            }
            viewReminderDeleteButton.setOnClickListener {
                viewModel.deleteEvent()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    UiEvent.Delete -> {
                        val action = ViewReminderFragmentDirections
                            .actionViewReminderFragmentToBottomRemindersFragment(
                                NavigationEvent.Delete(
                                    bundleOf("event" to viewModel.currentEvent.value.toEventRepeatNotificationAttachment())
                                )
                            )

                        view.findNavController().navigate(action)
                    }
                    is UiEvent.Edit -> {
                        view.findNavController().navigate(
                            R.id.createAndEditReminderFragment, event.passObject, NavOptions.Builder()
                                .setPopUpTo(R.id.viewReminderFragment, true)
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
                        viewReminderTitle.text = "${event.title}(${event.chapter}/${event.chapters})"
                    else
                        viewReminderTitle.text = event.title
                    if (event.allDay) {
                        viewReminderStartDateTime.visibility = View.GONE
                        viewReminderStartDateAndTimeDivider.visibility = View.GONE
                    }
                    viewReminderStartDateDate.text = event.startDateTime.time.toDate()
                    viewReminderStartDateTime.text = event.startDateTime.time.toTimeZone()
                    if (event.repeatEnd != Date(0)) {
                        viewReminderRepeat.text = resources.getString(event.repeatTitle) +
                                getString(R.string.until) +
                                event.repeatEnd.time.toDate()
                    } else {
                        viewReminderRepeat.text = resources.getString(event.repeatTitle)
                    }

                    var titleString = ""
                    event.notificationTitle.forEach { title ->
                        titleString += if (event.notificationTitle.size == 1)
                            getString(title)
                        else
                            getString(title) + "; "
                    }

                    viewReminderNotification.text = titleString

                    if (event.attachments.isNotEmpty())
                        viewReminderAttachmentRoot.visibility = View.VISIBLE
                    else
                        viewReminderAttachmentRoot.visibility = View.GONE
                    viewReminderColor.setCardBackgroundColor(event.color)
                    if (event.cover != Uri.EMPTY) {
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(space.id, View.VISIBLE)
                            constraintSet?.applyTo(motionBase)
                        }
                        Glide
                            .with(viewReminderCover.context)
                            .load(event.cover)
                            .into(viewReminderCover)
                    } else {
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(space.id, View.GONE)
                            constraintSet?.applyTo(motionBase)
                        }
                        viewReminderCover.setImageDrawable(null)
                    }
                }
            }
        }
        binding.apply {
            viewReminderAttachmentContentRoot.apply {
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