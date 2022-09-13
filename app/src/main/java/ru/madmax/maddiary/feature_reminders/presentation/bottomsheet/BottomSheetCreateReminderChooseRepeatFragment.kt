package ru.madmax.maddiary.feature_reminders.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.madmax.maddiary.R
import ru.madmax.maddiary.databinding.FragmentBtoomSheetCreateReminderChooseRepeatBinding
import ru.madmax.maddiary.feature_reminders.presentation.create_and_edit_event.CreateAndEditEventViewModel

@AndroidEntryPoint
class BottomSheetCreateReminderChooseRepeatFragment(
    private val viewModel: CreateAndEditEventViewModel
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBtoomSheetCreateReminderChooseRepeatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBtoomSheetCreateReminderChooseRepeatBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.apply {
                    when (event.repeat) {
                        bottomSheetRepeatNever.text.toString() -> {
                            bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_never)
                        }
                        bottomSheetRepeatEveryDay.text.toString() -> {
                            bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_day)
                        }
                        bottomSheetRepeatEverySecondDay.text.toString() -> {
                            bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_second_day)
                        }
                        bottomSheetRepeatEveryWeek.text.toString() -> {
                            bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_week)
                        }
                        bottomSheetRepeatEveryMonth.text.toString() -> {
                            bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_month)
                        }
                        bottomSheetRepeatEveryYear.text.toString() -> {
                            bottomSheetRepeatRbRoot.check(R.id.bottom_sheet_repeat_every_year)
                        }
                    }
                }
            }
        }

        binding.apply {
            bottomSheetRepeatRbRoot.setOnCheckedChangeListener { _, radioButtonId ->
                when (radioButtonId) {
                    R.id.bottom_sheet_repeat_never -> {
                        viewModel.updateRepeat(bottomSheetRepeatNever.text.toString())
                    }
                    R.id.bottom_sheet_repeat_every_day -> {
                        viewModel.updateRepeat(bottomSheetRepeatEveryDay.text.toString())
                    }
                    R.id.bottom_sheet_repeat_every_second_day -> {
                        viewModel.updateRepeat(bottomSheetRepeatEverySecondDay.text.toString())
                    }
                    R.id.bottom_sheet_repeat_every_week -> {
                        viewModel.updateRepeat(bottomSheetRepeatEveryWeek.text.toString())
                    }
                    R.id.bottom_sheet_repeat_every_month -> {
                        viewModel.updateRepeat(bottomSheetRepeatEveryMonth.text.toString())
                    }
                    R.id.bottom_sheet_repeat_every_year -> {
                        viewModel.updateRepeat(bottomSheetRepeatEveryYear.text.toString())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}