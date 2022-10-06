package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.databinding.FragmentCalendarBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.presentation.calendarView.CalendarDate
import ru.lopata.madDiary.featureReminders.presentation.calendarView.CalendarPagerAdapter
import ru.lopata.madDiary.featureReminders.presentation.calendarView.Month
import ru.lopata.madDiary.featureReminders.util.DayType
import java.time.LocalDate
import java.time.YearMonth

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

        /*binding.calendarViewPager.apply {
            adapter = calendarPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateSelectedMonthIndex(position)
                }
            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.listNoteState.collect { state ->
                    calendarPagerAdapter.submitList(state.calendar)
                    binding.monthYearTV.text = state.today.monthYear()
                    binding.calendarViewPager.setCurrentItem(state.currentMonthIndex, true)
                }
            }
        }

        binding.calendarNextMonthBtn.setOnClickListener {
            viewModel.toNextMonth()
        }

        binding.calendarPrevMonthBtn.setOnClickListener {
            viewModel.toPrevMonth()
        }*/


        /*binding.calendarRecyclerView.apply {
            adapter = calendarAdapter
            layoutManager = object : GridLayoutManager(requireActivity(), 7) {
                override fun canScrollVertically(): Boolean = false
                override fun canScrollHorizontally(): Boolean = false
            }
            addItemDecoration(CalendarGridDecoration())
        }*/
        binding.calendar.month = Month(
            YearMonth.of(2022, 10),
            days = listOf(
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 26)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 27)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 28)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 29)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 9, 30)
                ),
                CalendarDate(
                    dateType = DayType.TODAY,
                    day = LocalDate.of(2022, 10, 1)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 2)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 3)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 4)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 5)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 6),
                    events = listOf(
                        Event(
                            title = "Купить продукты",
                            color = Color.RED
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.GREEN
                        )
                    )
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 7),
                    events = listOf(
                        Event(
                            title = "Купить продукты",
                            color = Color.BLUE
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.MAGENTA
                        ),
                        Event(
                            title = "Купить еду",
                            color = Color.YELLOW
                        )
                    )
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 8)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 9)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 10)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 11)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 12)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 13)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 14)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 15)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 16)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 17)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 18)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 19)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 20)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 21)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 22)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 23)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 24)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 25)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 26)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 27)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 28)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 29)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 30)
                ),
                CalendarDate(
                    dateType = DayType.CURRENT_MONTH,
                    day = LocalDate.of(2022, 10, 31)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 1)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 2)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 3)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 4)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 5)
                ),
                CalendarDate(
                    dateType = DayType.NOT_CURRENT_MONTH,
                    day = LocalDate.of(2022, 11, 6)
                ),
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}