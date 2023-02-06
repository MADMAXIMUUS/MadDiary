package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.databinding.FragmentBottomSheetDatePickerBinding
import java.time.ZoneId

class BottomSheetDatePickerFragment(
    private var value: Long,
    private val requestKey: String,
    private val resultKey: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDatePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDatePickerBinding.inflate(
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
            bottomSheetDateCalendar.date = value
            bottomSheetDateCalendar.setOnDateChangeListener { _, year, month, day ->
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(year, month, day, 0, 0, 0)
                calendar.apply {
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.MILLISECONDS_IN_DAY, 0)
                }
                calendar.timeZone = TimeZone.getDefault()
                value = calendar.timeInMillis
                System.currentTimeMillis()
            }
            bottomSheetDateCalendar.firstDayOfWeek = Calendar.MONDAY
            bottomSheetDateChooseBtn.setOnClickListener {
                if (value == 0L) {
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR, 0)
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.MILLISECOND, 0)
                        set(Calendar.MILLISECONDS_IN_DAY, 0)
                    }
                    calendar.timeZone = TimeZone.getDefault()
                    setFragmentResult(
                        requestKey,
                        bundleOf(resultKey to calendar.timeInMillis)
                    )
                } else {
                    setFragmentResult(
                        requestKey,
                        bundleOf(resultKey to value)
                    )
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