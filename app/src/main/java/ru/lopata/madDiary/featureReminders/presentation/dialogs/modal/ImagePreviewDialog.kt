package ru.lopata.madDiary.featureReminders.presentation.dialogs.modal

import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentPreviewImageDialogBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.OnAttachmentDialogListener
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.ImageItemState

@AndroidEntryPoint
class ImagePreviewDialog(
    private val item: ImageItemState,
    private var isChosen: Boolean,
    private val listener: OnAttachmentDialogListener
) : DialogFragment() {

    private var _binding: FragmentPreviewImageDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme_full)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val width = getWindowWidth()
        val height = getWindowHeight()
        val layoutParams = binding.root.layoutParams
        layoutParams.width = width
        layoutParams.height = height
        binding.root.layoutParams = layoutParams

        binding.imagePreviewBackButton.setOnClickListener {
            dismiss()
        }

        if (isChosen) {
            binding.imagePreviewChooseButton.speed = 3f
            binding.imagePreviewChooseButton.setMinAndMaxProgress(0.0f, 0.5f)
            binding.imagePreviewChooseButton.playAnimation()
        }

        binding.imagePreviewChooseButton.setOnClickListener {
            if (!isChosen) {
                isChosen = true
                binding.imagePreviewChooseButton.speed = 1.5f
                binding.imagePreviewChooseButton.setMinAndMaxProgress(0.0f, 0.5f)
                binding.imagePreviewChooseButton.playAnimation()
            } else {
                isChosen = false
                binding.imagePreviewChooseButton.speed = 1.5f
                binding.imagePreviewChooseButton.setMinAndMaxProgress(0.5f, 1.0f)
                binding.imagePreviewChooseButton.playAnimation()
            }
            listener.onImageChosen(item, isChosen)
        }

        Glide
            .with(binding.imagePreviewImage.context)
            .load(item.uri)
            .into(binding.imagePreviewImage)
    }

    private fun getWindowHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    private fun getWindowWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}