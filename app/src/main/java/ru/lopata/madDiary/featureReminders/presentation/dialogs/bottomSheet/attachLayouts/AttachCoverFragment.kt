package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.lopata.madDiary.core.util.AttachItemDecoration
import ru.lopata.madDiary.databinding.FragmentAttachCoverBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.CoverAdapter
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.OnAttachmentChosenListener

class AttachCoverFragment(
    private val covers: List<Uri>,
    private val chosenCover: Uri,
    private val listener: OnAttachmentChosenListener?
) : Fragment() {

    private var _binding: FragmentAttachCoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachCoverBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CoverAdapter(listener)
        adapter.submitList(covers)
        adapter.updateChosen(chosenCover)
        binding.apply {
            bottomSheetRv.apply {
                this.adapter = adapter
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(AttachItemDecoration(20, 2))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}