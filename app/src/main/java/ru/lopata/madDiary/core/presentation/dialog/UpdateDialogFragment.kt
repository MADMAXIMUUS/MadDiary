package ru.lopata.madDiary.core.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.core.domain.model.UpdateModel
import ru.lopata.madDiary.databinding.FragmentUpdateDialogBinding

class UpdateDialogFragment(
    private val update: UpdateModel
) : BottomSheetDialogFragment() {

    private var _binding: FragmentUpdateDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}