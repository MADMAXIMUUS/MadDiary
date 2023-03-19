package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.databinding.FragmentCalendarBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.EventInCalendarGrid
import ru.lopata.madDiary.featureReminders.presentation.calendarView.MadCalendarMonth
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.SelectedDateEventsDialogFragment
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CalendarFragment : Fragment(), SelectedDateEventsDialogFragment.OnEventClickListener {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        val calendarPagerAdapter = CalendarPagerAdapter(requireActivity())

        binding.calendarViewPager.apply {
            adapter = calendarPagerAdapter.apply {
                setOnDayClickListener(object : CalendarPagerAdapter.OnDayClickListener {
                    override fun onDayCLick(
                        view: MadCalendarMonth, day: Calendar, events: List<EventInCalendarGrid>
                    ) {
                        if (day.get(Calendar.MONTH) > viewModel.uiState.value.selectedDay.get(
                                Calendar.MONTH
                            ) || day.get(Calendar.YEAR) > viewModel.uiState.value.selectedDay.get(
                                Calendar.YEAR
                            )
                        ) {
                            when (val drawable = binding.calendarNextMonthBtn.drawable) {
                                is AnimatedVectorDrawable -> {
                                    drawable.reset()
                                    drawable.start()
                                }
                            }
                        } else if (day.get(Calendar.MONTH) < viewModel.uiState.value.selectedDay.get(
                                Calendar.MONTH
                            ) || day.get(Calendar.YEAR) < viewModel.uiState.value.selectedDay.get(
                                Calendar.YEAR
                            )
                        ) {
                            when (val drawable = binding.calendarPrevMonthBtn.drawable) {
                                is AnimatedVectorDrawable -> {
                                    drawable.reset()
                                    drawable.start()
                                }
                            }
                        }
                        SelectedDateEventsDialogFragment(
                            this@CalendarFragment, day, events
                        ).show(
                            requireActivity().supportFragmentManager, "CalendarEventsDialog"
                        )
                        viewModel.changeSelectedDate(day)
                    }
                })
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position > viewModel.uiState.value.currentPosition) {
                        when (val drawable = binding.calendarNextMonthBtn.drawable) {
                            is AnimatedVectorDrawable -> {
                                drawable.reset()
                                drawable.start()
                            }
                        }
                    } else if (position < viewModel.uiState.value.currentPosition) {
                        when (val drawable = binding.calendarPrevMonthBtn.drawable) {
                            is AnimatedVectorDrawable -> {
                                drawable.reset()
                                drawable.start()
                            }
                        }
                    }
                    viewModel.updateSelectedMonthIndex(position)
                }
            })
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest {
                viewModel.uiState.collectLatest { state ->

                    calendarPagerAdapter.submitList(state.calendar)
                    binding.monthYearTV.text = SimpleDateFormat(
                        "LLLL yyyy", resources.configuration.locales.get(0)
                    ).format(state.selectedDay.time)

                    binding.calendarViewPager.setCurrentItem(
                        state.currentPosition, state.isNeedAnimation
                    )
                }
            }
        }

        binding.calendarNextMonthBtn.setOnClickListener {
            when (val drawable = binding.calendarNextMonthBtn.drawable) {
                is AnimatedVectorDrawable -> {
                    drawable.reset()
                    drawable.start()
                }
            }
            viewModel.toNextMonth()
        }

        binding.calendarPrevMonthBtn.setOnClickListener {
            when (val drawable = binding.calendarPrevMonthBtn.drawable) {
                is AnimatedVectorDrawable -> {
                    drawable.reset()
                    drawable.start()
                }
            }
            viewModel.toPrevMonth()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(eventId: Int, type: Event.Types) {
        val action = when (type) {
            Event.Types.EVENT -> {
                CalendarFragmentDirections.actionBottomCalendarToViewEventFragment(eventId, -1, -1)
            }
            Event.Types.TASK -> {
                CalendarFragmentDirections.actionBottomCalendarToViewTaskFragment(eventId)
            }
            Event.Types.REMINDER -> {
                CalendarFragmentDirections.actionBottomCalendarToViewReminderFragment(eventId)
            }
        }
        navController.navigate(action)
    }

    override fun onCheckClick(eventId: Int, state: Boolean) {
        viewModel.updateCheckState(eventId, state)
    }

    override fun onNewClick(date: Long) {
        val action = CalendarFragmentDirections
            .actionBottomCalendarToBottomSheetChooseReminderTypeFragment(date)
        navController.navigate(action)
    }

}