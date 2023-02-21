package ru.lopata.madDiary.featureReminders.presentation.createAndEditTask

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.BuildConfig
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.domain.service.CopyAttachmentService
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.databinding.FragmentCreateAndEditTaskBinding
import ru.lopata.madDiary.featureReminders.domain.model.Notification
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.model.states.AudioItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.FileItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.ImageItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.VideoItemState
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.*
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.AttachmentAdapter
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.OnAttachmentDialogListener
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.AudioPreviewDialog
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.ImagePreviewDialog
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.VideoPreviewDialog
import ru.lopata.madDiary.featureReminders.util.AndroidAlarmScheduler
import java.sql.Date

@AndroidEntryPoint
class CreateAndEditTaskFragment : Fragment(), OnAttachmentDialogListener {

    private var _binding: FragmentCreateAndEditTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAndEditTaskViewModel by viewModels()
    private var isEdit = false

    private lateinit var imageDialog: ImagePreviewDialog
    private lateinit var videoDialog: VideoPreviewDialog
    private lateinit var audioDialog: AudioPreviewDialog

    private val permResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        var allAreGranted = true
        for (perm in result.values) {
            allAreGranted = allAreGranted && perm
        }

        if (allAreGranted) {
            getPhotos()
            getVideos()
        } else {
            requireActivity().showPermissionDialog(
                resources.getString(R.string.permission_dialog_text)
            ) {
                showAppSettingsScreen()
            }
        }
    }

    private val fileResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val file: Intent? = result.data
            if (file != null) {
                if (file.data != null) {
                    var fileName = ""
                    val resolver = requireActivity().contentResolver
                    file.data.let { returnUri ->
                        resolver.query(returnUri!!, null, null, null)
                    }?.use { cursor ->
                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        fileName = cursor.getString(nameIndex)
                    }
                    val fileDescriptor = resolver.openFileDescriptor(file.data!!, "r")
                    val fileSize = fileDescriptor?.statSize
                    val type = resolver.getType(file.data!!).toString()
                    viewModel.updateAddedFiles(
                        FileItemState(
                            uri = file.data!!,
                            size = fileSize ?: 0,
                            name = fileName,
                            type = type,
                        )
                    )
                    viewModel.updateAttachment()
                }
            }
        }
    }

    private fun getPhotos() {
        CoroutineScope(Dispatchers.IO).launch {
            getGalleryImagesPath()
            this.cancel()
        }
    }

    private fun getVideos() {
        CoroutineScope(Dispatchers.IO).launch {
            getGalleryVideoPath()
            this.cancel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY, this
        ) { _, bundle ->
            if (bundle.getLong("startDate") != 0L) viewModel.updateStartDate(bundle.getLong("startDate"))

            if (bundle.getBoolean("startTimeSet")) viewModel.updateStartTime(bundle.getLong("startTime"))

            if (bundle.getString("note") != null) viewModel.updateNote(bundle.getString("note")!!)

            if (bundle.getInt("repeatTitle") != 0) {
                viewModel.updateRepeat(bundle.getLong("repeat"))
                viewModel.updateRepeatTitle(bundle.getInt("repeatTitle"))
                if (bundle.getLong("repeat") != Repeat.NO_REPEAT) {
                    if (viewModel.currentEvent.value.repeatEnd == Date(0)) {
                        BottomSheetDatePickerFragment(
                            viewModel.currentEvent.value.startDate,
                            REQUEST_KEY,
                            "repeatEnd"
                        ).show(
                            requireActivity().supportFragmentManager, "DatePickerDialog"
                        )
                    } else {
                        BottomSheetDatePickerFragment(
                            viewModel.currentEvent.value.repeatEnd.time,
                            REQUEST_KEY,
                            "repeatEnd"
                        ).show(
                            requireActivity().supportFragmentManager, "DatePickerDialog"
                        )
                    }
                }
            }

            if (bundle.getLong("repeatEnd") != 0L) {
                viewModel.updateRepeatEnd(bundle.getLong("repeatEnd"))
            }

            if (bundle.getInt("color") != 0) viewModel.updateColor(bundle.getInt("color"))

            if (bundle.getIntArray("notificationsTitle") != null) {
                viewModel.updateNotificationTitle(bundle.getIntArray("notificationsTitle")!!)
                viewModel.updateNotifications(bundle.getLongArray("notifications")!!)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireActivity().checkPermission(Manifest.permission.READ_MEDIA_IMAGES)) {
                getPhotos()
            }
            if (requireActivity().checkPermission(Manifest.permission.READ_MEDIA_VIDEO)) {
                getVideos()
            }
        } else {
            if (requireActivity().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                getPhotos()
                getVideos()
            }
        }
        requireActivity().hideKeyboard()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAndEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attachmentAdapter = AttachmentAdapter(this)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isEdit) {
                        isEnabled = true
                    } else {
                        isEnabled = false
                        view.findNavController().navigateUp()
                    }
                }

            })

        binding.apply {
            createAndEditTaskBackButton.setOnClickListener {
                view.findNavController().navigateUp()
            }

            createAndEditTaskConfirmButton.setOnClickListener {
                viewModel.saveEvent()
            }

            createAndEditTaskDeleteButton.setOnClickListener {
                viewModel.deleteEvent()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.Save -> {
                        val intent = Intent(requireContext(), CopyAttachmentService::class.java)
                        intent.putExtra("eventId", event.id)
                        requireActivity().startService(intent)
                        ContextCompat.startForegroundService(requireContext(), intent)

                        if (viewModel.currentEvent.value.notifications[0] != Notification.NEVER) {

                            val channel = NotificationChannel(
                                "eventAlarm",
                                getString(R.string.event_chanel_name),
                                NotificationManager.IMPORTANCE_HIGH
                            ).apply {
                                description = getString(R.string.event_description)
                            }
                            val notificationManager =
                                requireContext().getSystemService(NotificationManager::class.java)
                            notificationManager.createNotificationChannel(channel)

                            val alarmScheduler = AndroidAlarmScheduler(requireContext())
                            alarmScheduler.schedule(viewModel.currentEvent.value.toEventRepeatNotificationAttachment(), "eventAlarm")
                        }

                        val action =
                            CreateAndEditTaskFragmentDirections.actionCreateAndEditTaskFragmentToBottomReminders(
                                NavigationEvent.Create(
                                    viewModel.currentEvent.value.title.text
                                )
                            )

                        view.findNavController().navigate(action)
                    }
                    is UiEvent.Update -> {
                        val action =
                            CreateAndEditTaskFragmentDirections.actionCreateAndEditTaskFragmentToBottomReminders(
                                NavigationEvent.Update(
                                    viewModel.currentEvent.value.title.text
                                )
                            )

                        view.findNavController().navigate(action)
                    }
                    is UiEvent.Delete -> {
                        val action =
                            CreateAndEditTaskFragmentDirections.actionCreateAndEditTaskFragmentToBottomReminders(
                                NavigationEvent.Delete(
                                    bundleOf("event" to viewModel.currentEvent.value.toEventRepeatNotificationAttachment())
                                )
                            )

                        view.findNavController().navigate(action)
                    }
                    else -> {}
                }
            }

        }

        binding.apply {
            createAndEditTaskAllDaySwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateAllDayState(isChecked)
            }
            createAndEditTaskDeleteCoverButton.setOnClickListener {
                viewModel.updateCover(Uri.EMPTY)
            }
            scrollView2.setOnScrollChangeListener { _, _, _, _, _ ->
                requireActivity().hideKeyboard()
            }
            createAndEditTaskAttachmentContentRoot.apply {
                adapter = attachmentAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL, false
                )
                addItemDecoration(HorizontalListsItemDecoration(10, 10))
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.apply {
                    attachmentAdapter.submitList(event.attachments)


                    if (binding.createAndEditTaskTitleEdt.text.isEmpty()) binding.createAndEditTaskTitleEdt.setText(
                        event.title.text
                    )

                    createAndEditTaskTitleEdt.addTextChangedListener(afterTextChanged = { text ->
                        viewModel.updateTitle(text.toString())
                    })
                    if (event.title.isError) {
                        createAndEditTaskTitleError.visibility = View.VISIBLE
                    } else {
                        createAndEditTaskTitleError.visibility = View.GONE
                    }
                    if (event.isStartDateError) {
                        createAndEditTaskStartDateRoot.strokeWidth = 5
                    } else {
                        createAndEditTaskStartDateRoot.strokeWidth = 0
                    }
                    if (event.startDate != 0L || event.startTime != 0L) {
                        createAndEditTaskStartDateDate.text = event.startDate.toDate()
                        createAndEditTaskStartDateTime.text = event.startTime.toTime()
                        createAndEditTaskStartDateTime.visibility = View.VISIBLE
                    }
                    if (event.allDay) {
                        createAndEditTaskStartDateTime.visibility = View.GONE
                        createAndEditTaskStartDateAndTimeDivider.visibility = View.GONE
                    } else if (event.startDate != 0L) {
                        createAndEditTaskStartDateTime.visibility = View.VISIBLE
                        createAndEditTaskStartDateAndTimeDivider.visibility = View.VISIBLE
                    }

                    createAndEditTaskRepeat.text = getString(event.repeatTitle)

                    if (event.repeatEnd != Date(0)) {
                        createAndEditTaskRepeat.text =
                            createAndEditTaskRepeat.text.toString() + getString(R.string.until) + event.repeatEnd.time.toDate()
                    }

                    if (event.color != -1) {
                        createAndEditTaskColor.setCardBackgroundColor(event.color)
                    }
                    if (event.chosenCover != Uri.EMPTY) {
                        scrollView2.smoothScrollTo(0, 0)
                        motionBase.constraintSetIds.forEach {
                            val constraintSet = motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(motionHead.id, View.VISIBLE)
                            constraintSet?.applyTo(motionBase)
                        }
                        Glide.with(createAndEditTaskCover.context).load(event.chosenCover)
                            .into(createAndEditTaskCover)
                    } else {
                        createAndEditTaskCover.setImageDrawable(null)
                        motionBase.constraintSetIds.forEach {
                            val constraintSet = motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(motionHead.id, View.GONE)
                            constraintSet?.applyTo(motionBase)
                        }
                    }
                    createAndEditTaskStartDateTime.setOnClickListener {
                        BottomSheetTimePickerFragment(
                            event.startTime, REQUEST_KEY, "startTime"
                        ).show(
                            requireActivity().supportFragmentManager, "BottomSheetChooseTime"
                        )
                        createAndEditTaskStartDateAndTimeDivider.visibility = View.VISIBLE
                    }
                    createAndEditTaskNote.text = event.note
                    createAndEditTaskAllDaySwitch.isChecked = event.allDay

                    var titleString = ""
                    event.notificationTitle.forEach { title ->
                        titleString += if (event.notificationTitle.size == 1) getString(title)
                        else getString(title) + "; "
                    }

                    createAndEditTaskNotification.text = titleString

                    createAndEditTaskStartDateDate.setOnClickListener {
                        BottomSheetDatePickerFragment(
                            event.startDate, REQUEST_KEY, "startDate"
                        ).show(
                            requireActivity().supportFragmentManager, "BottomSheetChooseDate"
                        )
                    }

                    createAndEditTaskLocationRoot.setOnClickListener {
                        /*BottomSheetInputLocationFragment()
                            .show(
                                requireActivity().supportFragmentManager,
                                "BottomSheetLocation"
                            )*/
                    }

                    createAndEditTaskNoteRoot.setOnClickListener {
                        BottomSheetAddNoteFragment(event.note, REQUEST_KEY, "note").show(
                            requireActivity().supportFragmentManager, "BottomSheetNote"
                        )
                    }

                    createAndEditTaskRepeatRoot.setOnClickListener {
                        BottomSheetChooseRepeatFragment(event.repeat, REQUEST_KEY, "repeat").show(
                            requireActivity().supportFragmentManager, "BottomSheetRepeat"
                        )
                    }

                    createAndEditTaskNotificationRoot.setOnClickListener {
                        BottomSheetChooseNotificationFragment(
                            event.notifications, REQUEST_KEY, "notifications"
                        ).show(
                            requireActivity().supportFragmentManager, "BottomSheetNotification"
                        )
                    }

                    createAndEditTaskColor.setOnClickListener {
                        BottomSheetChooseColorFragment(
                            event.color, REQUEST_KEY, "color"
                        ).show(
                            requireActivity().supportFragmentManager, "BottomSheetColor"
                        )
                    }

                    createAndEditTaskAttachmentRoot.setOnClickListener {
                        BottomSheetAttachmentRootFragment.Builder().covers(event.covers)
                            .chosenCover(event.chosenCover).images(event.images)
                            .chosenImages(event.chosenImages).videos(event.videos)
                            .chosenVideos(event.chosenVideos).audios(event.audios)
                            .files(event.files)
                            .onAttachmentChosenListener(this@CreateAndEditTaskFragment).build()
                            .show(
                                requireActivity().supportFragmentManager, "BottomSheetAttachment"
                            )
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private suspend fun getGalleryImagesPath() = coroutineScope {
        launch {
            val imagePathList = mutableListOf<ImageItemState>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.MediaColumns._ID, MediaStore.Video.VideoColumns.SIZE
            )
            val sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
            val cursor: Cursor? =
                requireContext().contentResolver.query(uri, projection, null, null, sortOrder)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id: Int =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val size =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE))
                    imagePathList.add(
                        ImageItemState(
                            uri = ContentUris.withAppendedId(uri, id.toLong()), size = size
                        )
                    )
                }
                cursor.close()
            }
            viewModel.updateImages(imagePathList)
        }
    }

    private suspend fun getGalleryVideoPath() = coroutineScope {
        launch {
            val videoPathList = mutableListOf<VideoItemState>()
            val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE
            )
            val sortOrder = MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC"
            val cursor: Cursor? =
                requireContext().contentResolver.query(uri, projection, null, null, sortOrder)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id: Int =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val duration =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
                    val size =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE))
                    videoPathList.add(
                        VideoItemState(
                            uri = ContentUris.withAppendedId(uri, id.toLong()),
                            duration = duration,
                            size = size
                        )
                    )
                }
                cursor.close()
            }
            viewModel.updateVideos(videoPathList)
        }
    }

    private fun showAppSettingsScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onPermissionButtonClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permResultLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            )
        } else {
            permResultLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    override fun onInternalButtonClick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        fileResultLauncher.launch(intent)
    }

    override fun onCoverChosen(uri: Uri) {
        viewModel.updateCover(uri)
    }

    override fun onImagesChosen(items: List<ImageItemState>) {
        viewModel.updateChosenImages(items)
        viewModel.updateAttachment()
    }

    override fun onImageChosen(item: ImageItemState, state: Boolean) {
        viewModel.updateChosenImage(item, state)
        viewModel.updateAttachment()
    }

    override fun onVideosChosen(items: List<VideoItemState>) {
        viewModel.updateChosenVideos(items)
        viewModel.updateAttachment()
    }

    override fun onVideoChosen(item: VideoItemState, state: Boolean) {
        viewModel.updateChosenVideo(item, state)
        viewModel.updateAttachment()
    }

    override fun onAudioChosen(item: AudioItemState) {
        viewModel.updateAddedAudios(item)
        viewModel.updateAttachment()
    }

    override fun onFileChosen(item: FileItemState) {
        viewModel.updateAddedFiles(item)
        viewModel.updateAttachment()
    }

    override fun onImageDialogShow(item: ImageItemState, isChosen: Boolean) {
        imageDialog = ImagePreviewDialog(item, isChosen, this)
        imageDialog.show(
            requireActivity().supportFragmentManager, "MediaPreviewDialog"
        )
    }

    override fun onVideoDialogShow(item: VideoItemState, isChosen: Boolean) {
        videoDialog = VideoPreviewDialog(item, isChosen, this)
        videoDialog.show(
            requireActivity().supportFragmentManager, "MediaPreviewDialog"
        )
    }

    override fun onAudioDialogShow(item: AudioItemState, isChosen: Boolean) {
        audioDialog = AudioPreviewDialog(item, isChosen, this)
        audioDialog.show(
            requireActivity().supportFragmentManager, "MediaPreviewDialog"
        )
    }

    companion object {
        const val REQUEST_KEY = "createTask"
    }
}