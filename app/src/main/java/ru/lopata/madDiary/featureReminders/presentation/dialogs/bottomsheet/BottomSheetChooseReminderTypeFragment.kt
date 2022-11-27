package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentBottomSheetChooseReminderTypeBinding


class BottomSheetChooseReminderTypeFragment(private val navController: NavController) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetChooseReminderTypeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetChooseReminderTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetCreateReminderEventBtn.setOnClickListener {
            navController.navigate(R.id.action_bottom_reminders_to_createReminderFragment)
            dismiss()
        }

        binding.bottomSheetCreateReminderEventRoot
            .viewTreeObserver
            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    if (binding.bottomSheetCreateReminderReminderRoot.measuredWidth > 0)
                        binding.bottomSheetCreateReminderReminderRoot.viewTreeObserver.removeOnGlobalLayoutListener(
                            this
                        )

                    val width = binding.bottomSheetCreateReminderReminderRoot.measuredWidth
                    binding.bottomSheetCreateReminderTaskRoot.layoutParams.width = width
                    binding.bottomSheetCreateReminderTaskRoot.requestLayout()
                    binding.bottomSheetCreateReminderEventRoot.layoutParams.width = width
                    binding.bottomSheetCreateReminderEventRoot.requestLayout()

                }
            })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}