package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentBtoomSheetCreateReminderChooseRepeatBinding
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.CreateAndEditEventViewModel

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
                        Repeat.NO_REPEAT-> {
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
            }
        }

        binding.apply {
            bottomSheetRepeatRbRoot.setOnCheckedChangeListener { _, radioButtonId ->
                when (radioButtonId) {
                    R.id.bottom_sheet_repeat_never -> {
                        viewModel.updateRepeat(Repeat.NO_REPEAT)
                        viewModel.updateRepeatTitle(R.string.never)
                    }
                    R.id.bottom_sheet_repeat_every_day -> {
                        viewModel.updateRepeat(Repeat.EVERY_DAY)
                        viewModel.updateRepeatTitle(R.string.every_day)
                    }
                    R.id.bottom_sheet_repeat_every_second_day -> {
                        viewModel.updateRepeat(Repeat.EVERY_SECOND_DAY)
                        viewModel.updateRepeatTitle(R.string.every_second_day)
                    }
                    R.id.bottom_sheet_repeat_every_week -> {
                        viewModel.updateRepeat(Repeat.EVERY_WEEK)
                        viewModel.updateRepeatTitle(R.string.every_week)
                    }
                    R.id.bottom_sheet_repeat_every_second_week -> {
                        viewModel.updateRepeat(Repeat.EVERY_SECOND_WEEK)
                        viewModel.updateRepeatTitle(R.string.every_second_week)
                    }
                    R.id.bottom_sheet_repeat_every_month -> {
                        viewModel.updateRepeat(Repeat.EVERY_MONTH)
                        viewModel.updateRepeatTitle(R.string.every_month)
                    }
                    R.id.bottom_sheet_repeat_every_year -> {
                        viewModel.updateRepeat(Repeat.EVERY_YEAR)
                        viewModel.updateRepeatTitle(R.string.every_year)
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