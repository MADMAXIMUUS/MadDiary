package ru.madmax.madnotes.presentation.list_reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.madmax.madnotes.R
import ru.madmax.madnotes.databinding.FragmentListCategoryBinding
import ru.madmax.madnotes.databinding.FragmentListReminderBinding


class ListReminderFragment : Fragment() {

    private var _binding: FragmentListReminderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}