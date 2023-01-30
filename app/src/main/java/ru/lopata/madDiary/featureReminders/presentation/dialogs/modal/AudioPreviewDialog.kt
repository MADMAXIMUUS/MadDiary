package ru.lopata.madDiary.featureReminders.presentation.dialogs.modal

import android.annotation.SuppressLint
import android.graphics.Insets
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.toTimeDuration
import ru.lopata.madDiary.databinding.FragmentPreviewAudioDialogBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.OnAttachmentDialogListener
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.AudioItemState
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class AudioPreviewDialog(
    private val item: AudioItemState,
    private var isChosen: Boolean,
    private val listener: OnAttachmentDialogListener
) : DialogFragment() {

    private var _binding: FragmentPreviewAudioDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: MediaPlayer
    private val outputFile = File(item.uri.path.toString())

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme_full)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewAudioDialogBinding.inflate(inflater, container, false)
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
            listener.onAudioChosen(item)
        }

        binding.mediaPreviewAudio.visibility = View.VISIBLE
        player = MediaPlayer()
        player.apply {
            setDataSource(outputFile.absolutePath)
            prepare()
            setOnCompletionListener {
                binding.mediaPreviewAudioPlayAndPauseButton.speed = -4f
                binding.mediaPreviewAudioPlayAndPauseButton.playAnimation()
                binding.mediaPreviewAudioCassette.pauseAnimation()
                handler.removeCallbacks(runnable)
                seekTo(0)
                this@AudioPreviewDialog.isPlaying = false
                binding.mediaPreviewAudioSeekbar.progress = 0
                binding.mediaPreviewAudioDuration.text = "00:00"
            }
        }
        binding.mediaPreviewAudioSeekbar.max = player.duration
        binding.mediaPreviewAudioDurationMax.text = player.duration.toTimeDuration()
        binding.mediaPreviewAudioDuration.text = "00:00"

        setHandler()

        binding.mediaPreviewAudioPlayAndPauseButton.setOnClickListener {
            when {
                isPlaying -> pauseAudio()
                else -> playAudio()
            }
        }

        binding.mediaPreviewAudioSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser)
                    player.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }

    private fun setHandler() {
        runnable = Runnable {
            binding.mediaPreviewAudioSeekbar.progress = player.currentPosition
            binding.mediaPreviewAudioDuration.text = player.currentPosition.toTimeDuration()
            handler.postDelayed(runnable, 0)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun playAudio() {
        player.start()
        binding.mediaPreviewAudioPlayAndPauseButton.speed = 4f
        binding.mediaPreviewAudioPlayAndPauseButton.playAnimation()
        binding.mediaPreviewAudioCassette.playAnimation()
        isPlaying = true
    }

    private fun pauseAudio() {
        player.pause()
        binding.mediaPreviewAudioPlayAndPauseButton.speed = -4f
        binding.mediaPreviewAudioPlayAndPauseButton.playAnimation()
        binding.mediaPreviewAudioCassette.pauseAnimation()
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
        if (this::player.isInitialized) {
            try {
                player.release()
            } catch (e: IOException) {
                e.localizedMessage?.let { Log.e("MediaPlayer", it) }
            }
        }
    }
}