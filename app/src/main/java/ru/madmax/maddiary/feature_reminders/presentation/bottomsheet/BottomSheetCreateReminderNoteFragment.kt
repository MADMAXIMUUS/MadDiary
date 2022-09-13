package ru.madmax.maddiary.feature_reminders.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.maddiary.databinding.FragmentBottomSheetCreateReminderNoteBinding
import ru.madmax.maddiary.feature_reminders.presentation.create_and_edit_event.CreateAndEditEventViewModel

@AndroidEntryPoint
class BottomSheetCreateReminderNoteFragment(
    private val viewModel: CreateAndEditEventViewModel
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCreateReminderNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetCreateReminderNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collect { event ->
                binding.bottomSheetNoteContent.setText(event.note)
                binding.bottomSheetNoteContent.setSelection(event.note.length)
            }
        }
        binding.bottomSheetNoteContent.addTextChangedListener(afterTextChanged = { note ->
            viewModel.updateNote(note.toString())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}