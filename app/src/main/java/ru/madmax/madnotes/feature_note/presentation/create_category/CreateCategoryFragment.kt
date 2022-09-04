package ru.madmax.madnotes.feature_note.presentation.create_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.madmax.madnotes.databinding.FragmentCreateAndEditCategoryBinding

class CreateCategoryFragment : Fragment() {

    private var _binding: FragmentCreateAndEditCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAndEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}