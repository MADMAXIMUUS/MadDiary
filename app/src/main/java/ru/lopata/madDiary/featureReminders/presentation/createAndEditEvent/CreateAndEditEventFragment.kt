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
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.UiEvent
import ru.lopata.madDiary.core.util.isDarkTheme
import ru.lopata.madDiary.core.util.setNavigationColor
import ru.lopata.madDiary.core.util.toDateTime
import ru.lopata.madDiary.databinding.FragmentCreateAndEditEventBinding
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet.*
import java.util.*

@AndroidEntryPoint
class CreateAndEditEventFragment : Fragment() {

    private var _binding: FragmentCreateAndEditEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAndEditEventViewModel by viewModels()

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
                    if (event.startDateTime != Date(0)) {
                        val date = event.startDateTime.time.toDateTime().split(" ")[0]
                        val time = event.startDateTime.time.toDateTime().split(" ")[1]
                        createAndEditEventStartDateDate.text = date
                        createAndEditEventStartDateTime.text = time
                    }
                    if (event.endDateTime != Date(0)) {
                        val date = event.endDateTime.time.toDateTime().split(" ")[0]
                        val time = event.endDateTime.time.toDateTime().split(" ")[1]
                        createAndEditEventEndDateDate.text = date
                        createAndEditEventEndDateTime.text = time
                    }
                    if (event.allDay) {
                        createAndEditEventEndDateTime.visibility = View.GONE
                        createAndEditEventStartDateTime.visibility = View.GONE
                        createAndEditEventEndDateAndTimeDivider.visibility = View.GONE
                        createAndEditEventStartDateAndTimeDivider.visibility = View.GONE
                    }
                    createAndEditEventNote.text = event.note
                    createAndEditEventAllDaySwitch.isChecked = event.allDay
                    createAndEditEventRepeat.text = getString(event.repeatTitle)
                    createAndEditEventNotification.text = event.notification
                    createAndEditEventStartDateRoot.setOnClickListener {
                        BottomSheetCreateReminderDatePickerFragment(event.startDateTime) { startDate ->
                            if (!event.allDay) {
                                BottomSheetCreateReminderTimePickerFragment(startDate) { date ->
                                    viewModel.updateStartTimestamp(date)
                                    createAndEditEventStartDateTime.visibility = View.VISIBLE
                                    createAndEditEventStartDateAndTimeDivider.visibility =
                                        View.VISIBLE
                                }.show(
                                    requireActivity().supportFragmentManager,
                                    "BottomSheetChooseTime"
                                )
                            } else {
                                viewModel.updateStartTimestamp(startDate)
                            }
                        }.show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
                        )
                    }
                    createAndEditEventEndDateRoot.setOnClickListener {
                        BottomSheetCreateReminderDatePickerFragment(event.endDateTime) { endDate ->
                            if (!event.allDay) {
                                BottomSheetCreateReminderTimePickerFragment(endDate) { date ->
                                    viewModel.updateEndTimestamp(date)
                                    createAndEditEventEndDateTime.visibility = View.VISIBLE
                                    createAndEditEventEndDateAndTimeDivider.visibility =
                                        View.VISIBLE
                                }.show(
                                    requireActivity().supportFragmentManager,
                                    "BottomSheetChooseTime"
                                )
                            } else {
                                viewModel.updateEndTimestamp(endDate)
                            }
                        }.show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
                        )
                    }

                    createAndEditEventNoteRoot.setOnClickListener {
                        BottomSheetCreateReminderNoteFragment(viewModel).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetNote"
                        )
                    }

                    createAndEditEventAllDaySwitch.setOnCheckedChangeListener { _, isChecked ->
                        viewModel.updateAllDayState(isChecked)
                    }

                    createAndEditEventRepeatRoot.setOnClickListener {
                        BottomSheetCreateReminderChooseRepeatFragment(viewModel).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetRepeat"
                        )
                    }

                    createAndEditEventNotificationRoot.setOnClickListener {
                        BottomSheetCreateReminderChooseNotificationFragment(viewModel).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetNotification"
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

}