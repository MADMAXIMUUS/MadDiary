package ru.madmax.maddiary.feature_reminders.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.maddiary.databinding.FragmentBottomSheetCteateReminderDatePickerBinding
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class BottomSheetCreateReminderDatePickerFragment(
    private var date: Date,
    private val onChoose: (Date) -> Unit
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
            bottomSheetDateCalendar.date = date.time
            bottomSheetDateCalendar.setOnDateChangeListener { _, year, month, day ->
                val calender: Calendar = Calendar.getInstance()
                calender.set(year, month, day, 0, 0, 0)
                calender.timeZone = TimeZone.getDefault()
                date = Date(calender.timeInMillis)
                System.currentTimeMillis()
            }
            bottomSheetDateChooseBtn.setOnClickListener {
                if (date == Date(0))
                    onChoose(
                        Date.from(
                            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
                        )
                    )
                else
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