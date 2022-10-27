package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.databinding.FragmentBottomSheetCteateReminderDatePickerBinding
import java.sql.Date

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
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, month, day, 0, 0, 0)
                calendar.apply {
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.MILLISECONDS_IN_DAY, 0)
                }
                calendar.timeZone = TimeZone.getDefault()
                date = Date(calendar.timeInMillis)
                System.currentTimeMillis()
            }
            bottomSheetDateChooseBtn.setOnClickListener {
                if (date == Date(0)) {
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR, 0)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.MILLISECOND, 0)
                        set(Calendar.MILLISECONDS_IN_DAY, 0)
                    }
                    calendar.timeZone = TimeZone.getDefault()
                    onChoose(
                        Date(calendar.timeInMillis)
                    )
                } else {
                    onChoose(date)
                }
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}