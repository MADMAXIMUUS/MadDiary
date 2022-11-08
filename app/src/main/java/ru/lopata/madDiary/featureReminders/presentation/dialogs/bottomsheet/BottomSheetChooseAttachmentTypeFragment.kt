package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentBottomSheetChooseAttachmentTypeBinding


class BottomSheetChooseAttachmentTypeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetChooseAttachmentTypeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetChooseAttachmentTypeBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}