package ru.madmax.madnotes.presentation.list_category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.madmax.madnotes.R
import ru.madmax.madnotes.databinding.FragmentCreateCategoryBinding
import ru.madmax.madnotes.databinding.FragmentListCategoryBinding

class ListCategoryFragment : Fragment() {


    private var _binding: FragmentListCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}