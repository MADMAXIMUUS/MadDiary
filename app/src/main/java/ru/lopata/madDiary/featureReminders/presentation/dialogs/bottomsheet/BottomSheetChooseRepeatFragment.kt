package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentBtoomSheetChooseRepeatBinding
import ru.lopata.madDiary.featureReminders.domain.model.Repeat

class BottomSheetChooseRepeatFragment(
    private val repeat: Long,
    private val requestKey: String,
    private val resultKey: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBtoomSheetChooseRepeatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBtoomSheetChooseRepeatBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            when (repeat) {
                Repeat.NO_REPEAT -> {
                    bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_never)
                }
                Repeat.EVERY_DAY -> {
                    bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_day)
                }
                Repeat.EVERY_SECOND_DAY -> {
                    bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_second_day)
                }
                Repeat.EVERY_WEEK -> {
                    bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_week)
                }
                Repeat.EVERY_SECOND_WEEK -> {
                    bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_second_week)
                }
                Repeat.EVERY_MONTH -> {
                    bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_month)
                }
                Repeat.EVERY_YEAR -> {
                    bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_year)
                }
            }
        }


        val bundle = Bundle()

        binding.apply {
            bottomSheetRepeatRbRoot.setOnCheckedChangeListener { _, radioButtonId ->
                when (radioButtonId) {
                    R.id.bottom_sheet_repeat_never -> {
                        bundle.putInt(resultKey + "Title", R.string.never)
                        bundle.putLong(resultKey, Repeat.NO_REPEAT)
                    }
                    R.id.bottom_sheet_repeat_every_day -> {
                        bundle.putInt(resultKey + "Title", R.string.every_day)
                        bundle.putLong(resultKey, Repeat.EVERY_DAY)
                    }
                    R.id.bottom_sheet_repeat_every_second_day -> {
                        bundle.putInt(resultKey + "Title", R.string.every_second_day)
                        bundle.putLong(resultKey, Repeat.EVERY_SECOND_DAY)
                    }
                    R.id.bottom_sheet_repeat_every_week -> {
                        bundle.putInt(resultKey + "Title", R.string.every_week)
                        bundle.putLong(resultKey, Repeat.EVERY_WEEK)
                    }
                    R.id.bottom_sheet_repeat_every_second_week -> {
                        bundle.putInt(resultKey + "Title", R.string.every_second_week)
                        bundle.putLong(resultKey, Repeat.EVERY_SECOND_WEEK)
                    }
                    R.id.bottom_sheet_repeat_every_month -> {
                        bundle.putInt(resultKey + "Title", R.string.every_month)
                        bundle.putLong(resultKey, Repeat.EVERY_MONTH)
                    }
                    R.id.bottom_sheet_repeat_every_year -> {
                        bundle.putInt(resultKey + "Title", R.string.every_year)
                        bundle.putLong(resultKey, Repeat.EVERY_YEAR)
                    }
                }
            }
            bottomSheetTimeChooseBtn.setOnClickListener {
                setFragmentResult(requestKey, bundle)
                dismiss()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}