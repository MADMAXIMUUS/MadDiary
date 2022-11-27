package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.lopata.madDiary.databinding.FragmentCalendarBinding
import ru.lopata.madDiary.featureReminders.presentation.calendarView.MadCalendarMonth
import java.text.SimpleDateFormat

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val calendarAdapter = CalendarAdapter { day ->
            viewModel.changeSelectedDate(day.day)
            if (day.events.isNotEmpty()) {
                SelectedDateEventsDialogFragment(day).show(
                    requireActivity().supportFragmentManager,
                    "SelectedDateEventsDialog"
                )
            }
        }*/

        val calendarPagerAdapter = CalendarPagerAdapter(requireActivity())


        binding.calendarViewPager.apply {
            adapter = calendarPagerAdapter.apply {
                setOnDayClickListener(object : CalendarPagerAdapter.OnDayClickListener {
                    override fun onDayCLick(view: MadCalendarMonth, day: Calendar) {
                        viewModel.changeSelectedDate(day)
                    }
                })
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateSelectedMonthIndex(position)
                }
            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    calendarPagerAdapter.submitList(state.calendar)
                    binding.monthYearTV.text = SimpleDateFormat(
                        "LLLL yyyy",
                        resources.configuration.locales.get(0)
                    ).format(state.selectedDay.time)

                    binding.calendarViewPager.setCurrentItem(
                        state.currentPosition,
                        state.isNeedAnimation
                    )
                }
            }
        }

        binding.calendarNextMonthBtn.setOnClickListener {
            viewModel.toNextMonth()
        }

        binding.calendarPrevMonthBtn.setOnClickListener {
            viewModel.toPrevMonth()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}