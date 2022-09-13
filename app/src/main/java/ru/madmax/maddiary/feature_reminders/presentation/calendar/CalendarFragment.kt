package ru.madmax.maddiary.feature_reminders.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.madmax.maddiary.core.util.CalendarGridDecoration
import ru.madmax.maddiary.core.util.monthYear
import ru.madmax.maddiary.databinding.FragmentCalendarBinding

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

        val calendarAdapter = CalendarAdapter { day ->
            viewModel.changeSelectedDate(day.day)
                if (day.events.isNotEmpty()) {
                    SelectedDateEventsDialogFragment(day).show(
                        requireActivity().supportFragmentManager,
                        "SelectedDateEventsDialog"
                    )
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.listNoteState.collect { state ->
                    calendarAdapter.submitList(state.calendar)
                    binding.monthYearTV.text = state.selectedDate.monthYear()
                }
            }
        }

        binding.calendarNextMonthBtn.setOnClickListener {
            viewModel.toNextMonth()
        }

        binding.calendarPrevMonthBtn.setOnClickListener {
            viewModel.toPrevMonth()
        }


        binding.calendarRecyclerView.apply {
            adapter = calendarAdapter
            layoutManager = object : GridLayoutManager(requireActivity(), 7) {
                override fun canScrollVertically(): Boolean = false
                override fun canScrollHorizontally(): Boolean = false
            }
            addItemDecoration(CalendarGridDecoration())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}