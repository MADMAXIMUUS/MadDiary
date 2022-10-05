package ru.lopata.madDiary.featureReminders.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.madDiary.databinding.FragmentBottomSheetCreateReminderTimePickerBinding
import java.util.*

@AndroidEntryPoint
class BottomSheetCreateReminderTimePickerFragment(
    private val dateTime: Date,
    val onChoose: (Date) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCreateReminderTimePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetCreateReminderTimePickerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bottomSheetTimePickerHr.minValue = 0
            bottomSheetTimePickerHr.maxValue = 23
            bottomSheetTimePickerHr.setFormatter {
                String.format("%02d", it)
            }
            bottomSheetTimePickerMin.minValue = 0
            bottomSheetTimePickerMin.maxValue = 59
            bottomSheetTimePickerMin.setFormatter {
                String.format("%02d", it)
            }
            bottomSheetTimeChooseBtn.setOnClickListener {
                val timestampHr = bottomSheetTimePickerHr.value * 60 * 60 * 1000
                val timestampMin = bottomSheetTimePickerMin.value * 60 * 1000
                val timeStamp = Date(dateTime.time + timestampHr + timestampMin)
                onChoose(timeStamp)
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}