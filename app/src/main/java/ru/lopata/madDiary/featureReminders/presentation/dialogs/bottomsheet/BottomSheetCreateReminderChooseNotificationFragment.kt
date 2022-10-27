package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.databinding.FragmentBottomSheetCreateReminderChooseNotificationBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.CreateAndEditEventViewModel

@AndroidEntryPoint
class BottomSheetCreateReminderChooseNotificationFragment(
    private val viewModel: CreateAndEditEventViewModel
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCreateReminderChooseNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetCreateReminderChooseNotificationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}