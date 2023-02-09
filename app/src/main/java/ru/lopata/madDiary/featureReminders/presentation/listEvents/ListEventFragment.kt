package ru.lopata.madDiary.featureReminders.presentation.listEvents

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.ListsItemDecoration
import ru.lopata.madDiary.core.util.NavigationEvent
import ru.lopata.madDiary.databinding.FragmentListEventBinding
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.BottomSheetChooseReminderTypeFragment
import ru.lopata.madDiary.featureReminders.util.AndroidAlarmScheduler
import java.io.File

@AndroidEntryPoint
class ListEventFragment : Fragment(), ListEventAdapter.OnItemClickListener {

    private var _binding: FragmentListEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel: ListEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        val eventAdapter = ListEventAdapter(this)

        binding.eventListRv.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListsItemDecoration(10, 10))
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest { state ->
                eventAdapter.submitList(state.events)
                binding.eventListRv.smoothScrollToPosition(state.todayId)
            }
        }

        binding.eventListBtnCreateReminder.setOnClickListener {
            BottomSheetChooseReminderTypeFragment(navController).show(
                requireActivity().supportFragmentManager,
                "ChooseReminderType"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        when (val event = ListEventFragmentArgs.fromBundle(requireArguments()).navigationEvent) {
            is NavigationEvent.Delete -> {
                val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    event.passObject.getParcelable(
                        "event",
                        EventRepeatNotificationAttachment::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    event.passObject.getParcelable("event")
                }
                if (item != null) {
                    viewModel.visibleDelete(item)
                    Snackbar
                        .make(
                            binding.root, getString(
                                R.string.event_created,
                                item.event.title
                            ), Snackbar.LENGTH_SHORT
                        )
                        .setAnchorView(binding.eventListBtnCreateReminder)
                        .setAction(R.string.undo) {
                            viewModel.undoDelete(item)
                        }
                        .addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    viewModel.delete(item.event)
                                    val alarmScheduler = AndroidAlarmScheduler(requireContext())
                                    alarmScheduler.cancel(item)
                                    item.attachments.forEach { attachment ->
                                        Uri.parse(attachment.uri).path?.let { File(it).delete() }
                                    }
                                }
                                super.onDismissed(transientBottomBar, event)
                            }
                        })
                        .show()
                }
            }
            is NavigationEvent.Create -> {
                Snackbar
                    .make(
                        binding.root, getString(
                            R.string.event_created,
                            event.name
                        ), Snackbar.LENGTH_SHORT
                    )
                    .setAnchorView(binding.eventListBtnCreateReminder)
                    .show()
            }
            is NavigationEvent.Update -> {
                Snackbar
                    .make(
                        binding.root, getString(
                            R.string.event_updated,
                            event.name
                        ), Snackbar.LENGTH_SHORT
                    )
                    .setAnchorView(binding.eventListBtnCreateReminder)
                    .show()
            }
            else -> {}
        }

        requireArguments().clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(id: Int, chapter: Int, chapters: Int) {
        val action = ListEventFragmentDirections
            .actionBottomRemindersToViewEventFragment(
                eventId = id,
                chapter = chapter,
                chapters = chapters
            )
        navController.navigate(action)
    }

    override fun onItemCheckedClick(id: Int, state: Boolean) {
        viewModel.updateEventState(id, state)
    }
}