package ru.madmax.madnotes.presentation.create_reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.madmax.madnotes.R
import ru.madmax.madnotes.databinding.FragmentCreateReminderBinding
import ru.madmax.madnotes.databinding.FragmentListNoteBinding

class CreateReminderFragment : Fragment() {

    private var _binding: FragmentCreateReminderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}