package ru.lopata.madDiary.featureReminders.presentation.dialogs.modal

import android.annotation.SuppressLint
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.toTimeDuration
import ru.lopata.madDiary.databinding.FragmentPreviewVideoDialogBinding
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.OnAttachmentDialogListener
import ru.lopata.madDiary.featureReminders.domain.model.states.VideoItemState

@AndroidEntryPoint
class VideoPreviewDialog(
    private val item: VideoItemState,
    private var isChosen: Boolean,
    private val listener: OnAttachmentDialogListener
) : DialogFragment() {

    private var _binding: FragmentPreviewVideoDialogBinding? = null
    private val binding get() = _binding!!

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private var isPlaying = false
    private var isHide = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme_full)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewVideoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
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
            listener.onVideoChosen(item, isChosen)
        }
        binding.mediaPreviewVideo.visibility = View.VISIBLE
        binding.mediaPreviewVideoPlayer.setVideoURI(item.uri)
        binding.mediaPreviewVideoPlayer.setOnPreparedListener {
            binding.mediaPreviewVideoSeekbar.max = binding.mediaPreviewVideoPlayer.duration
            binding.mediaPreviewVideoDurationMax.text =
                binding.mediaPreviewVideoPlayer.duration.toTimeDuration()
            binding.mediaPreviewVideoDuration.text = "00:00"
            binding.mediaPreviewVideoPlayer.seekTo(1)
        }
        binding.mediaPreviewVideoPlayer.setOnCompletionListener {
            binding.mediaPreviewVideoPlayPause.speed = -4f
            binding.mediaPreviewVideoPlayPause.playAnimation()
            isPlaying = false
            binding.mediaPreviewVideoSeekbar.progress = 1
            binding.mediaPreviewVideoPlayer.seekTo(1)
        }
        binding.mediaPreviewVideoPlayer.setOnClickListener {
            if (isHide) {
                binding.mediaPreviewVideoPlayPause.visibility = View.VISIBLE
                binding.mediaPreviewVideoControlsRoot.visibility = View.VISIBLE
                binding.imagePreviewChooseButton.visibility = View.VISIBLE
                binding.imagePreviewBackButton.visibility = View.VISIBLE
                isHide = false
            } else {
                binding.mediaPreviewVideoPlayPause.visibility = View.GONE
                binding.mediaPreviewVideoControlsRoot.visibility = View.GONE
                binding.imagePreviewChooseButton.visibility = View.GONE
                binding.imagePreviewBackButton.visibility = View.GONE
                isHide = true
            }
        }
        binding.mediaPreviewVideoPlayPause.setOnClickListener {
            when {
                isPlaying -> pauseVideo()
                else -> playVideo()
            }
        }
        binding.mediaPreviewVideoSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser)
                    binding.mediaPreviewVideoPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        setHandler()
    }

    private fun setHandler() {
        runnable = Runnable {
            binding.mediaPreviewVideoSeekbar.progress =
                binding.mediaPreviewVideoPlayer.currentPosition
            binding.mediaPreviewVideoDuration.text =
                binding.mediaPreviewVideoPlayer.currentPosition.toTimeDuration()
            handler.postDelayed(runnable, 0)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun playVideo() {
        binding.mediaPreviewVideoPlayer.start()
        binding.mediaPreviewVideoPlayPause.speed = 4f
        binding.mediaPreviewVideoPlayPause.playAnimation()
        isPlaying = true
    }

    private fun pauseVideo() {
        binding.mediaPreviewVideoPlayer.pause()
        binding.mediaPreviewVideoPlayPause.speed = -4f
        binding.mediaPreviewVideoPlayPause.playAnimation()
        isPlaying = false
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
        if (this::runnable.isInitialized)
            handler.removeCallbacks(runnable)
    }
}