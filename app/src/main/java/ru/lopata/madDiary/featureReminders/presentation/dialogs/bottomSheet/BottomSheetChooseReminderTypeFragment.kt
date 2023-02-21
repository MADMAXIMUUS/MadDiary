package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.databinding.FragmentBottomSheetChooseReminderTypeBinding

class BottomSheetChooseReminderTypeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetChooseReminderTypeBinding? = null
    private val binding get() = _binding!!

    private var date = -1L

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
            val action = BottomSheetChooseReminderTypeFragmentDirections
                .actionGlobalCreateAndEditEventFragment(startDate = date)
            this.findNavController().popBackStack()
            this.findNavController().navigate(action)
        }

        binding.bottomSheetCreateReminderTaskBtn.setOnClickListener {
            val action =
                BottomSheetChooseReminderTypeFragmentDirections
                    .actionGlobalCreateAndEditTaskFragment(startDate = date)
            this.findNavController().popBackStack()
            this.findNavController().navigate(action)

        }

        binding.bottomSheetCreateReminderReminderBtn.setOnClickListener {
            val action =
                BottomSheetChooseReminderTypeFragmentDirections
                    .actionGlobalCreateAndEditReminderFragment(startDate = date)
            this.findNavController().popBackStack()
            this.findNavController().navigate(action)
        }

        binding.bottomSheetCreateReminderEventRoot
            .viewTreeObserver
            .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    if (binding.bottomSheetCreateReminderReminderRoot.measuredWidth > 0)
                        binding.bottomSheetCreateReminderReminderRoot
                            .viewTreeObserver
                            .removeOnGlobalLayoutListener(this)

                    val width = binding.bottomSheetCreateReminderReminderRoot.measuredWidth
                    binding.bottomSheetCreateReminderTaskRoot.layoutParams.width = width
                    binding.bottomSheetCreateReminderTaskRoot.requestLayout()
                    binding.bottomSheetCreateReminderEventRoot.layoutParams.width = width
                    binding.bottomSheetCreateReminderEventRoot.requestLayout()

                }
            })

    }

    override fun onResume() {
        super.onResume()
        date = BottomSheetChooseReminderTypeFragmentArgs.fromBundle(requireArguments()).date
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}