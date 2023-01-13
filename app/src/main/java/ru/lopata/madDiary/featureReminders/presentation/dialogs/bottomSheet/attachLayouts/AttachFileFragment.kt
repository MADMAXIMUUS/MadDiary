package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.lopata.madDiary.core.util.ListsItemDecoration
import ru.lopata.madDiary.databinding.FragmentAttachFileBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.FileAdapter
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.OnAttachmentChosenListener
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.FileItemState

class AttachFileFragment(
    private val files: List<FileItemState>,
    private val listener: OnAttachmentChosenListener
) : Fragment() {

    private var _binding: FragmentAttachFileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachFileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FileAdapter(listener)
        adapter.submitList(files)
        binding.apply {
            bottomSheetInternal.setOnClickListener {
                listener.onInternalButtonClick()
            }
            bottomSheetRv.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(ListsItemDecoration(20,20))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}