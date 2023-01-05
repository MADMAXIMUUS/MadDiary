package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.EventColors
import ru.lopata.madDiary.databinding.FragmentBottomSheetChooseColorBinding

class BottomSheetChooseColorFragment(
    private val color: Int,
    private val requestKey: String,
    private val resultKey: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetChooseColorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetChooseColorBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            when (color) {
                EventColors.PURPLE -> {
                    bottomSheetChooseColorRbRoot.check(R.id.bottom_sheet_choose_color_purple)
                }
                EventColors.ORANGE -> {
                    bottomSheetChooseColorRbRoot.check(R.id.bottom_sheet_choose_color_yellow)
                }
                EventColors.BLUE -> {
                    bottomSheetChooseColorRbRoot.check(R.id.bottom_sheet_choose_color_blue)
                }
                EventColors.RED -> {
                    bottomSheetChooseColorRbRoot.check(R.id.bottom_sheet_choose_color_pink)
                }
                EventColors.GREEN -> {
                    bottomSheetChooseColorRbRoot.check(R.id.bottom_sheet_choose_color_green)
                }
                EventColors.DEFAULT -> {
                    bottomSheetChooseColorRbRoot.check(R.id.bottom_sheet_choose_color_default)
                }
            }
        }

        binding.bottomSheetChooseColorPurple.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragmentResult(
                    requestKey,
                    bundleOf(resultKey to EventColors.PURPLE)
                )
                dismiss()
            }
        }
        binding.bottomSheetChooseColorYellow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragmentResult(
                    requestKey,
                    bundleOf(resultKey to EventColors.ORANGE)
                )
                dismiss()
            }
        }
        binding.bottomSheetChooseColorGreen.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragmentResult(
                    requestKey,
                    bundleOf(resultKey to EventColors.GREEN)
                )
                dismiss()
            }
        }
        binding.bottomSheetChooseColorBlue.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragmentResult(
                    requestKey,
                    bundleOf(resultKey to EventColors.BLUE)
                )
                dismiss()
            }
        }
        binding.bottomSheetChooseColorPink.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragmentResult(
                    requestKey,
                    bundleOf(resultKey to EventColors.RED)
                )
                dismiss()
            }
        }
        binding.bottomSheetChooseColorDefault.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setFragmentResult(
                    requestKey,
                    bundleOf(resultKey to EventColors.DEFAULT)
                )
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}