package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.databinding.FragmentBottomSheetTimePickerBinding
import java.sql.Date

@AndroidEntryPoint
class BottomSheetTimePickerFragment(
    private val time: Long,
    private val requestKey: String,
    private val resultKey: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetTimePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetTimePickerBinding.inflate(
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
                val timestampHr: Long = bottomSheetTimePickerHr.value * 60L * 60L * 1000L
                val timestampMin: Long = bottomSheetTimePickerMin.value * 60L * 1000L
                val timeStamp = timestampHr + timestampMin
                val bundle = Bundle()
                bundle.putBoolean(resultKey + "Set", true)
                bundle.putLong(resultKey, timeStamp)
                setFragmentResult(requestKey, bundle)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}