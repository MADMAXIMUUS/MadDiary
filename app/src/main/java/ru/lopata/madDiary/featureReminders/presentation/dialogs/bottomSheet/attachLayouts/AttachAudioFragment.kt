package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts

import android.Manifest.permission.RECORD_AUDIO
import android.icu.util.Calendar
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class AttachAudioFragment(
    private val audios: List<AudioItemState>,
    private val listener: OnAttachmentChosenListener
) : Fragment(), OnTimerTickListener {

    private var _binding: FragmentAttachAudioBinding? = null
    private val binding get() = _binding!!

    private var isConfirmButton = false
    private var isPauseButton = false

    private var isRecording = false
    private var isPause = false

    private lateinit var record: MediaRecorder
    private lateinit var outputFile: File

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
        timer = Timer(this)
        adapter.submitList(audios)
        record = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            MediaRecorder(requireContext())
        else
            MediaRecorder()
        binding.apply {

            bottomSheetMicAndPauseButton.setOnClickListener {
                when {
                    isRecording -> pauseRecording()
                    isPause -> resumeRecording()
                    else -> startRecording()
                }
            }
            bottomSheetRv.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(ListsItemDecoration(20, 20))
            }
        }
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
            val outputFilename = "audio_record_${date}.mp3"
            outputFile = File(requireActivity().cacheDir.absolutePath, outputFilename)
            outputFile
            if (!outputFile.exists()) {
                outputFile.createNewFile()
            }

            record.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)

                try {
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
            binding.bottomSheetTimer.apply {
                visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.stop()
        record.release()
        _binding = null
    }

    override fun onTimerTick(duration: String) {
        binding.bottomSheetTimer.text = duration
        binding.bottomSheetWave.addAmplitude(record.maxAmplitude.toFloat())
    }
}