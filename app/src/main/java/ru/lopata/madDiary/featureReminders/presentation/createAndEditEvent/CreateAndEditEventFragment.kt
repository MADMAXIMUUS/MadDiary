package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.databinding.FragmentCreateAndEditEventBinding
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet.*

@AndroidEntryPoint
class CreateAndEditEventFragment : Fragment() {

    private var _binding: FragmentCreateAndEditEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAndEditEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity()
            .supportFragmentManager
            .setFragmentResultListener(REQUEST_KEY, this) { _, bundle ->
                if (bundle.getLong("startDate") != 0L)
                    viewModel.updateStartDate(bundle.getLong("startDate"))

                if (bundle.getBoolean("startTimeSet"))
                    viewModel.updateStartTime(bundle.getLong("startTime"))

                if (bundle.getLong("endDate") != 0L)
                    viewModel.updateEndDate(bundle.getLong("endDate"))

                if (bundle.getBoolean("endTimeSet"))
                    viewModel.updateEndTime(bundle.getLong("endTime"))

                if (bundle.getString("note") != null)
                    viewModel.updateNote(bundle.getString("note")!!)

                if (bundle.getInt("repeatTitle") != 0) {
                    viewModel.updateRepeat(bundle.getLong("repeat"))
                    viewModel.updateRepeatTitle(bundle.getInt("repeatTitle"))
                }

                if (bundle.getInt("color") != 0)
                    viewModel.updateColor(bundle.getInt("color"))

                if (bundle.getIntArray("notificationsTitle") != null) {
                    viewModel.updateNotificationTitle(bundle.getIntArray("notificationsTitle")!!)
                    viewModel.updateNotifications(bundle.getLongArray("notifications")!!)
                }
            }
    }

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
        _binding = FragmentCreateAndEditEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.create_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.create_menu_save -> {
                        viewModel.saveEvent()
                    }
                    R.id.create_menu_delete -> {
                        viewModel.deleteEvent()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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
                    is UiEvent.Save -> {
                        view.findNavController().navigateUp()
                    }
                    is UiEvent.Delete -> {
                        view.findNavController().navigateUp()
                    }
                    else -> {}
                }
            }

        }

        binding.createAndEditEventTitleEdt.addTextChangedListener(
            onTextChanged = { text, start, _, _ ->
                viewModel.updateTitle(text.toString(), start)
            }
        )

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.createAndEditEventTitleEdt.setText(event.title.text)
                this.cancel()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.apply {
                    /*createAndEditEventTitleEdt.setText(event.title.text)
                    createAndEditEventTitleEdt.setSelection(event.title.cursorPosition)*/
                    if (event.title.isError) {
                        createAndEditEventTitleError.visibility = View.VISIBLE
                    } else {
                        createAndEditEventTitleError.visibility = View.GONE
                    }
                    if (event.isStartDateError) {
                        createAndEditEventStartDateRoot.strokeWidth = 5
                    } else {
                        createAndEditEventStartDateRoot.strokeWidth = 0
                    }
                    if (event.isEndDateError) {
                        createAndEditEventEndDateRoot.strokeWidth = 5
                    } else {
                        createAndEditEventEndDateRoot.strokeWidth = 0
                    }
                    if (event.startDate != 0L || event.startTime != 0L) {
                        createAndEditEventStartDateDate.text = event.startDate.toDate()
                        createAndEditEventStartDateTime.text = event.startTime.toTime()
                        createAndEditEventStartDateTime.visibility = View.VISIBLE
                    }
                    if (event.endDate != 0L || event.endTime != 0L) {
                        createAndEditEventEndDateDate.text = event.endDate.toDate()
                        createAndEditEventEndDateTime.text = event.endTime.toTime()
                        createAndEditEventEndDateTime.visibility = View.VISIBLE
                    }
                    if (event.allDay) {
                        createAndEditEventEndDateTime.visibility = View.GONE
                        createAndEditEventStartDateTime.visibility = View.GONE
                        createAndEditEventEndDateAndTimeDivider.visibility = View.GONE
                        createAndEditEventStartDateAndTimeDivider.visibility = View.GONE
                    } else if (event.startDate != 0L && event.endDate != 0L) {
                        createAndEditEventEndDateTime.visibility = View.VISIBLE
                        createAndEditEventStartDateTime.visibility = View.VISIBLE
                        createAndEditEventEndDateAndTimeDivider.visibility = View.VISIBLE
                        createAndEditEventStartDateAndTimeDivider.visibility = View.VISIBLE
                    }
                    if (event.color != -1) {
                        createAndEditEventColor.setCardBackgroundColor(event.color)
                    }
                    if (event.showStartTimeDialog) {
                        BottomSheetTimePickerFragment(
                            event.startTime,
                            REQUEST_KEY,
                            "startTime"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseTime"
                        )
                        createAndEditEventStartDateAndTimeDivider.visibility = View.VISIBLE
                    }
                    if (event.showEndTimeDialog) {
                        BottomSheetTimePickerFragment(
                            event.startTime,
                            REQUEST_KEY,
                            "endTime"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseTime"
                        )
                        createAndEditEventEndDateAndTimeDivider.visibility = View.VISIBLE
                    }
                    createAndEditEventNote.text = event.note
                    createAndEditEventAllDaySwitch.isChecked = event.allDay
                    createAndEditEventRepeat.text = getString(event.repeatTitle)

                    var titleString = ""
                    event.notificationTitle.forEach { title ->
                        if (event.notificationTitle.size == 1)
                            titleString += getString(title)
                        else
                            titleString += getString(title)+"; "
                    }

                    createAndEditEventNotification.text = titleString

                    createAndEditEventStartDateRoot.setOnClickListener {
                        BottomSheetDatePickerFragment(
                            event.startDate,
                            REQUEST_KEY,
                            "startDate"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
                        )
                    }

                    createAndEditEventEndDateRoot.setOnClickListener {
                        BottomSheetDatePickerFragment(
                            event.endDate,
                            REQUEST_KEY,
                            "endDate"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
                        )
                    }

                    createAndEditEventNoteRoot.setOnClickListener {
                        BottomSheetAddNoteFragment(event.note, REQUEST_KEY, "note").show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetNote"
                        )
                    }

                    createAndEditEventAllDaySwitch.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.updateAllDayState(isChecked)
                    }

                    createAndEditEventRepeatRoot.setOnClickListener {
                        BottomSheetChooseRepeatFragment(event.repeat, REQUEST_KEY, "repeat").show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetRepeat"
                        )
                    }

                    createAndEditEventNotificationRoot.setOnClickListener {
                        BottomSheetChooseNotificationFragment(
                            event.notifications,
                            REQUEST_KEY,
                            "notifications"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetNotification"
                        )
                    }

                    createAndEditEventColor.setOnClickListener {
                        BottomSheetChooseColorFragment(
                            event.color,
                            REQUEST_KEY,
                            "color"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetColor"
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY = "createEvent"
    }

}