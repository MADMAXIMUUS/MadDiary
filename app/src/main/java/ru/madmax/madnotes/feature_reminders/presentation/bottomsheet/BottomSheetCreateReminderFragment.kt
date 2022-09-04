package ru.madmax.madnotes.feature_reminders.presentation.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.madmax.madnotes.R
import ru.madmax.madnotes.databinding.FragmentBottomSheetCreateReminderBinding
import ru.madmax.madnotes.databinding.FragmentListNoteBinding

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