package ru.madmax.maddiary.feature_reminders.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.maddiary.R
import ru.madmax.maddiary.databinding.FragmentBottomSheetCreateReminderBinding

@AndroidEntryPoint
class BottomSheetCreateReminderFragment(private val navController: NavController) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCreateReminderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetCreateReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetCreateReminderEventBtn.setOnClickListener {
            navController.navigate(R.id.action_bottom_reminders_to_createReminderFragment)
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}