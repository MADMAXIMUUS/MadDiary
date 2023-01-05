package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.lopata.madDiary.core.util.AttachItemDecoration
import ru.lopata.madDiary.core.util.checkPermission
import ru.lopata.madDiary.databinding.FragmentAttachImageBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.ImageAdapter
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.OnAttachmentChosenListener
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.ImageItemState

class AttachImageFragment(
    private val images: List<ImageItemState>,
    private val chosenImages: List<ImageItemState>,
    private val listener: OnAttachmentChosenListener
) : Fragment() {

    private var _binding: FragmentAttachImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachImageBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ImageAdapter(listener)
        adapter.submitList(images)
        adapter.updateChosen(chosenImages)
        binding.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!requireActivity().checkPermission(READ_MEDIA_IMAGES)) {
                    bottomSheetPermissionMessage.visibility = View.VISIBLE
                    bottomSheetPermissionMessage.setOnClickListener {
                        listener.onPermissionButtonClick()
                    }
                } else {
                    bottomSheetPermissionMessage.visibility = View.GONE
                }
            } else {
                if (!requireActivity().checkPermission(READ_EXTERNAL_STORAGE)) {
                    bottomSheetPermissionMessage.visibility = View.VISIBLE
                    bottomSheetPermissionMessage.setOnClickListener {
                        listener.onPermissionButtonClick()
                    }
                } else {
                    bottomSheetPermissionMessage.visibility = View.GONE
                }
            }
            bottomSheetRv.apply {
                this.adapter = adapter
                layoutManager = GridLayoutManager(requireContext(), 3)
                addItemDecoration(AttachItemDecoration(10, 3))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}