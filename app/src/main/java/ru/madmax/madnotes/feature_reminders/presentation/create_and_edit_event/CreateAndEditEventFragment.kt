package ru.madmax.madnotes.feature_reminders.presentation.create_and_edit_event

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import ru.madmax.madnotes.R
import ru.madmax.madnotes.core.util.isDarkTheme
import ru.madmax.madnotes.core.util.setNavigationColor
import ru.madmax.madnotes.core.util.toDate
import ru.madmax.madnotes.databinding.FragmentCreateAndEditEventBinding
import ru.madmax.madnotes.feature_reminders.presentation.bottomsheet.BottomSheetCreateReminderDatePickerFragment
import ru.madmax.madnotes.feature_reminders.presentation.bottomsheet.BottomSheetCreateReminderTimePickerFragment
import java.util.*

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

    @RequiresApi(Build.VERSION_CODES.O)
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
                        //viewModel.saveNote()
                    }
                    R.id.create_menu_delete -> {

                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.apply {
                    if (event.startTimestamp != Date(0)) {
                        val date = event.startTimestamp.time.toDate().split(" ")[0]
                        val time = event.startTimestamp.time.toDate().split(" ")[1]
                        createAndEditEventStartDateDate.text = date
                        createAndEditEventStartDateTime.text = time
                    }
                    if (event.endTimestamp != Date(0)) {
                        val date = event.endTimestamp.time.toDate().split(" ")[0]
                        val time = event.endTimestamp.time.toDate().split(" ")[1]
                        createAndEditEventEndDateDate.text = date
                        createAndEditEventEndDateTime.text = time
                    }

                    createAndEditEventStartDateRoot.setOnClickListener {
                        BottomSheetCreateReminderDatePickerFragment(event.startTimestamp.time) { startDate ->
                            BottomSheetCreateReminderTimePickerFragment(
                                startDate,
                                event.startTimestamp.time
                            ) { date ->
                                viewModel.updateStartTimestamp(date)
                                createAndEditEventStartDateAndTimeDivider.visibility = View.VISIBLE
                            }.show(
                                requireActivity().supportFragmentManager,
                                "BottomSheetChooseTime"
                            )
                        }.show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
                        )
                    }
                    createAndEditEventEndDateRoot.setOnClickListener {
                        BottomSheetCreateReminderDatePickerFragment(event.endTimestamp.time) { endDate ->
                            BottomSheetCreateReminderTimePickerFragment(
                                endDate,
                                event.endTimestamp.time
                            ) { date ->
                                viewModel.updateEndTimestamp(date)
                                createAndEditEventEndDateAndTimeDivider.visibility = View.VISIBLE
                            }.show(
                                requireActivity().supportFragmentManager,
                                "BottomSheetChooseTime"
                            )
                        }.show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
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