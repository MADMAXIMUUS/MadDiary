package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.lopata.madDiary.core.util.GridItemDecoration
import ru.lopata.madDiary.core.util.checkPermission
import ru.lopata.madDiary.databinding.FragmentAttachVideoBinding
import ru.lopata.madDiary.featureReminders.domain.model.states.VideoItemState
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.OnAttachmentDialogListener
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.VideoAdapter

class AttachVideoFragment(
    private val videos: List<VideoItemState>,
    private val chosenVideos: List<VideoItemState>,
    private val listener: OnAttachmentDialogListener
) : Fragment() {
    private var _binding: FragmentAttachVideoBinding? = null
    private val binding get() = _binding!!

    private val videoAdapter = VideoAdapter(listener)

    fun updateChosen(video: VideoItemState, state: Boolean): List<VideoItemState> {
        val oldList = videoAdapter.getChosenItems()
        val newList = mutableListOf<VideoItemState>()
        newList.addAll(oldList)
        if (state) {
            newList.add(video)
        } else {
            newList.remove(video)
        }
        videoAdapter.updateChosen(newList)
        return newList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttachVideoBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoAdapter.submitList(videos)
        videoAdapter.updateChosen(chosenVideos)
        binding.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!requireActivity().checkPermission(READ_MEDIA_VIDEO)) {
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
                this.adapter = videoAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
                addItemDecoration(GridItemDecoration(10,80, 3))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}