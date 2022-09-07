package ru.madmax.madnotes.feature_reminders.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import ru.madmax.madnotes.databinding.FragmentBottomSheetCteateReminderDatePickerBinding
import ru.madmax.madnotes.feature_reminders.presentation.create_and_edit_event.CreateAndEditEventViewModel
import java.util.*

class BottomSheetCreateReminderDatePickerFragment(
    private var date: Long,
    private val onChoose: (Long) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCteateReminderDatePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetCteateReminderDatePickerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bottomSheetDateCalendar.minDate = bottomSheetDateCalendar.date
            bottomSheetDateCalendar.date = date
            bottomSheetDateCalendar.setOnDateChangeListener { _, year, month, day ->
                val calender: Calendar = Calendar.getInstance()
                calender.set(year, month, day)
                date = calender.timeInMillis
            }
            bottomSheetDateChooseBtn.setOnClickListener {
                onChoose(date)
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}