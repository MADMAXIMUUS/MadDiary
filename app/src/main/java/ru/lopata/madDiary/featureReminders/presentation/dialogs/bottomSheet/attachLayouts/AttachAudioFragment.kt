package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts

import android.Manifest.permission.RECORD_AUDIO
import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.core.util.Timer.OnTimerTickListener
import ru.lopata.madDiary.databinding.FragmentAttachAudioBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.AudioAdapter
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.OnAttachmentChosenListener
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.AudioItemState
import java.io.File
import java.io.IOException
import java.nio.file.Files

class AttachAudioFragment(
    private val audios: List<AudioItemState>,
    private val listener: OnAttachmentChosenListener
) : Fragment(), OnTimerTickListener {

    private var _binding: FragmentAttachAudioBinding? = null
    private val binding get() = _binding!!
    private var isStopButton = false

    private var isPauseButton = false

    private var isRecording = false
    private var isPause = false

    private lateinit var record: MediaRecorder
    private lateinit var player: MediaPlayer

    private lateinit var outputFile: File
    private var outputFilename: String = ""

    private lateinit var timer: Timer

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AudioAdapter(listener)
        adapter.submitList(audios)
        timer = Timer(this)
        binding.apply {
            bottomSheetMicAndPauseButton.setOnClickListener {
                when {
                    isRecording -> pauseRecording()
                    isPause -> resumeRecording()
                    else -> startRecording()
                }
            }
            bottomSheetListAndStopButton.setOnClickListener {
                when {
                    isStopButton -> stop()
                    else -> openList()
                }
            }
            bottomSheetCancelButton.setOnClickListener {
                delete()
            }
            bottomSheetRv.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(FileItemDecoration(80, 20))
            }
            bottomSheetConfirmButton.setOnClickListener {
                confirm()
            }
            bottomSheetPlayAndPauseButton.setOnClickListener {
                when {
                    isPauseButton -> pause()
                    else -> play()
                }
            }
            bottomSheetCancelPlayButton.setOnClickListener {
                delete()
            }
            bottomSheetSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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
    }

    private fun confirm() {
        listener.onAudioChosen(
            AudioItemState(
                name = outputFilename,
                duration = timer.duration,
                size = Files.size(outputFile.toPath()),
                uri = outputFile.toUri()
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun play() {
        timer.start()
        player.start()
        binding.bottomSheetPlayAndPauseButton.speed = 4f
        binding.bottomSheetPlayAndPauseButton.playAnimation()
        binding.bottomSheetCassette.playAnimation()
        isPauseButton = true
    }

    private fun pause() {
        player.pause()
        timer.pause()
        binding.bottomSheetPlayAndPauseButton.speed = -4f
        binding.bottomSheetPlayAndPauseButton.playAnimation()
        binding.bottomSheetCassette.pauseAnimation()
        isPauseButton = false
    }

    @SuppressLint("SetTextI18n")
    private fun delete() {
        timer.duration = 0
        timer.stop()

        binding.bottomSheetTimer.text = "00:00.000"
        binding.bottomSheetTimer.visibility = View.INVISIBLE

        binding.bottomSheetListAndStopButton.speed = -4f
        binding.bottomSheetListAndStopButton.playAnimation()

        binding.bottomSheetCancelButton.visibility = View.INVISIBLE

        binding.bottomSheetWave.clear()

        outputFile.delete()
        isRecording = false
        isStopButton = false
        isPause = false

        binding.bottomSheetMicAndPauseButton.speed = -4f
        binding.bottomSheetMicAndPauseButton.playAnimation()

        binding.bottomSheetRecordRoot.visibility = View.VISIBLE
        binding.bottomSheetPlayerRoot.visibility = View.INVISIBLE

        binding.bottomSheetSeekbar.progress = 0
        try {
            record.release()
        } catch (e: IOException) {
            e.localizedMessage?.let { Log.e("MediaRecorder", it) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun stop() {
        timer.duration = 0
        timer.stop()

        binding.bottomSheetTimer.text = "00:00"
        binding.bottomSheetTimer.visibility = View.INVISIBLE

        binding.bottomSheetWave.clear()

        binding.bottomSheetRecordRoot.visibility = View.INVISIBLE
        binding.bottomSheetPlayerRoot.visibility = View.VISIBLE

        binding.bottomSheetListAndStopButton.speed = -4f
        binding.bottomSheetListAndStopButton.playAnimation()

        binding.bottomSheetMicAndPauseButton.speed = -4f
        binding.bottomSheetMicAndPauseButton.playAnimation()

        binding.bottomSheetCancelPlayButton.speed = 4f
        binding.bottomSheetCancelPlayButton.playAnimation()

        binding.bottomSheetConfirmButton.speed = 4f
        binding.bottomSheetConfirmButton.playAnimation()

        isRecording = false
        isPause = false
        isStopButton = false

        initMediaPlayer()
    }

    @SuppressLint("SetTextI18n")
    private fun initMediaPlayer() {
        if (!this::player.isInitialized) {
            player = MediaPlayer()
            player.apply {
                setDataSource(outputFile.absolutePath)
                prepare()
                setOnCompletionListener {
                    binding.bottomSheetPlayAndPauseButton.speed = -4f
                    binding.bottomSheetPlayAndPauseButton.playAnimation()
                    binding.bottomSheetCassette.pauseAnimation()
                    timer.stop()
                    seekTo(0)
                    isPauseButton = false
                    binding.bottomSheetSeekbar.progress = 0
                    binding.bottomSheetTimerDuration.text = "00:00"
                }
            }
            binding.bottomSheetSeekbar.max = player.duration
            binding.bottomSheetTimerDurationMax.text = player.duration.toTimeDuration()
            binding.bottomSheetTimerDuration.text = "00:00"
        } else {
            if (binding.bottomSheetSeekbar.progress == 0) {
                player.reset()
                player.setDataSource(outputFile.absolutePath)
                player.prepare()
                binding.bottomSheetSeekbar.max = player.duration
                binding.bottomSheetTimerDurationMax.text = player.duration.toTimeDuration()
                binding.bottomSheetTimerDurationMax.visibility = View.VISIBLE
            }
        }
    }

    private fun openList() {
        binding.bottomSheetRv.visibility = View.VISIBLE
        binding.bottomSheetRecordRoot.visibility = View.INVISIBLE
    }

    private fun resumeRecording() {
        binding.bottomSheetMicAndPauseButton.speed = 4f
        binding.bottomSheetMicAndPauseButton.playAnimation()
        record.resume()
        timer.start()
        isPause = false
        isRecording = true
    }

    private fun pauseRecording() {
        binding.bottomSheetMicAndPauseButton.speed = -4f
        binding.bottomSheetMicAndPauseButton.playAnimation()
        record.pause()
        timer.pause()
        isPause = true
        isRecording = false
    }

    private fun startRecording() {
        if (!requireActivity().checkPermission(RECORD_AUDIO)) {
            requireActivity().requestPermissions(RECORD_AUDIO)
        } else {
            val date = Calendar.getInstance().timeInMillis.toFileName()
            outputFilename = "audio_record_${date}.mp3"
            outputFile = File(requireActivity().cacheDir.absolutePath, outputFilename)
            outputFile
            if (!outputFile.exists()) {
                outputFile.createNewFile()
            }
            record = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                MediaRecorder(requireContext())
            else
                @Suppress("DEPRECATION")
                MediaRecorder()
            record.apply {
                try {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setAudioEncodingBitRate(128000)
                    setAudioSamplingRate(96000)
                    setOutputFile(outputFile)
                    prepare()
                } catch (e: IOException) {
                    e.localizedMessage?.let { Log.e("MediaRecorder", it) }
                }
                start()
            }
            binding.bottomSheetMicAndPauseButton.speed = 4f
            binding.bottomSheetMicAndPauseButton.playAnimation()
            isRecording = true
            isPause = false
            timer.start()
            binding.bottomSheetTimer.visibility = View.VISIBLE
            binding.bottomSheetListAndStopButton.speed = 4f
            binding.bottomSheetListAndStopButton.playAnimation()
            isStopButton = true
            binding.bottomSheetCancelButton.visibility = View.VISIBLE
            binding.bottomSheetCancelButton.speed = 4f
            binding.bottomSheetCancelButton.playAnimation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.stop()
        if (this::record.isInitialized) {
            try {
                record.release()
            } catch (e: IOException) {
                e.localizedMessage?.let { Log.e("MediaRecorder", it) }
            }
        }
        if (this::player.isInitialized) {
            try {
                player.release()
            } catch (e: IOException) {
                e.localizedMessage?.let { Log.e("MediaPlayer", it) }
            }
        }
        _binding = null
    }

    override fun onTimerTick(duration: String) {
        if (binding.bottomSheetRecordRoot.visibility == View.VISIBLE) {
            binding.bottomSheetTimer.text = duration
            binding.bottomSheetWave.addAmplitude(record.maxAmplitude.toFloat())
        } else {
            binding.bottomSheetTimerDuration.text = player.currentPosition.toTimeDuration()
            binding.bottomSheetSeekbar.progress = player.currentPosition
            Log.e("MediaPlayer", player.currentPosition.toTimeDuration())
        }
    }
}