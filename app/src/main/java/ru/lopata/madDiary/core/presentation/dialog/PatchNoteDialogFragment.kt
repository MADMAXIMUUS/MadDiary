package ru.lopata.madDiary.core.presentation.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentPatchNoteDialogBinding
import ru.lopata.madDiary.databinding.FragmentUpdateDialogBinding

class PatchNoteDialogFragment(
    private val description: String
) : BottomSheetDialogFragment() {

    private var _binding: FragmentPatchNoteDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatchNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}