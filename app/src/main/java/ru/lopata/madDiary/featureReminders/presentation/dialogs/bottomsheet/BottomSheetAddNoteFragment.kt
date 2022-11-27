package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.databinding.FragmentBottomSheetAddNoteBinding

class BottomSheetAddNoteFragment(
    private val note: String,
    private val requestKey: String,
    private val resultKey: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetAddNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomSheetNoteContent.setText(note)
        binding.bottomSheetNoteChooseBtn.setOnClickListener {
            setFragmentResult(
                requestKey,
                bundleOf(resultKey to binding.bottomSheetNoteContent.text.toString())
            )
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}