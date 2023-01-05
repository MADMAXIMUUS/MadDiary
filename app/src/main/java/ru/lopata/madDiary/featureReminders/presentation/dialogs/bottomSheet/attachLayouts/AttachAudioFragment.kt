package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.lopata.madDiary.databinding.FragmentAttachAudioBinding

class AttachAudioFragment : Fragment() {

    private var _binding: FragmentAttachAudioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachAudioBinding.inflate(
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