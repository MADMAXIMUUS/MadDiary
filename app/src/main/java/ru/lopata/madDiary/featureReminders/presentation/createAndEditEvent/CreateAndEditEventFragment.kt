package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import android.Manifest.permission.*
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.view.ActionMode
import androidx.appcompat.view.ActionMode.Callback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.BuildConfig
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.presentation.MainActivity
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.databinding.BottomSheetAddBinding
import ru.lopata.madDiary.databinding.BottomSheetButtonBinding
import ru.lopata.madDiary.databinding.BottomSheetChooseAttachmentTypeBinding
import ru.lopata.madDiary.databinding.FragmentCreateAndEditEventBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.*
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.VideoItemState
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet.*
import ru.lopata.madDiary.featureReminders.util.BottomSheetCallback

@AndroidEntryPoint
class CreateAndEditEventFragment : Fragment(), OnAttachmentChosenListener {

    private var _binding: FragmentCreateAndEditEventBinding? = null
    private val binding get() = _binding!!

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val viewModel: CreateAndEditEventViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingType: BottomSheetChooseAttachmentTypeBinding
    private lateinit var bottomSheetButtonBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingButton: BottomSheetButtonBinding
    private lateinit var bottomSheetCoverBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingCover: BottomSheetAddBinding
    private lateinit var bottomSheetImageBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingImage: BottomSheetAddBinding
    private lateinit var bottomSheetVideoBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingVideo: BottomSheetAddBinding
    private lateinit var bottomSheetAudioBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingAudio: BottomSheetAddBinding
    private lateinit var bottomSheetFileBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingFile: BottomSheetAddBinding

    private var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        var allAreGranted = true
        for (perm in result.values) {
            allAreGranted = allAreGranted && perm
        }

        if (allAreGranted) {
            bindingImage.bottomSheetPermissionMessage.visibility = View.GONE
            bindingVideo.bottomSheetPermissionMessage.visibility = View.GONE
            getPhotos()
            getVideos()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.permission_dialog_title))
                .setMessage(resources.getString(R.string.permission_dialog_text))
                .setNegativeButton(resources.getString(R.string.cancel_button_title)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.settings_button_title)) { dialog, _ ->
                    showAppSettingsScreen()
                    dialog.dismiss()
                }
                .show()
        }
    }

    private lateinit var menuProvider: MenuProvider

    private lateinit var actionModCallBack: Callback

    private var contextActionMode: ActionMode? = null

    private var isNeedClose: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity()
            .supportFragmentManager
            .setFragmentResultListener(REQUEST_KEY, this) { _, bundle ->
                if (bundle.getLong("startDate") != 0L)
                    viewModel.updateStartDate(bundle.getLong("startDate"))

                if (bundle.getBoolean("startTimeSet"))
                    viewModel.updateStartTime(bundle.getLong("startTime"))

                if (bundle.getLong("endDate") != 0L)
                    viewModel.updateEndDate(bundle.getLong("endDate"))

                if (bundle.getBoolean("endTimeSet"))
                    viewModel.updateEndTime(bundle.getLong("endTime"))

                if (bundle.getString("note") != null)
                    viewModel.updateNote(bundle.getString("note")!!)

                if (bundle.getInt("repeatTitle") != 0) {
                    viewModel.updateRepeat(bundle.getLong("repeat"))
                    viewModel.updateRepeatTitle(bundle.getInt("repeatTitle"))
                }

                if (bundle.getInt("color") != 0)
                    viewModel.updateColor(bundle.getInt("color"))

                if (bundle.getIntArray("notificationsTitle") != null) {
                    viewModel.updateNotificationTitle(bundle.getIntArray("notificationsTitle")!!)
                    viewModel.updateNotifications(bundle.getLongArray("notifications")!!)
                }
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireActivity().checkPermission(READ_MEDIA_IMAGES)) {
                getPhotos()
            }
            if (requireActivity().checkPermission(READ_MEDIA_VIDEO)) {
                getVideos()
            }
        } else {
            if (requireActivity().checkPermission(READ_EXTERNAL_STORAGE)) {
                getPhotos()
                getVideos()
            }
        }
        requireActivity().hideKeyboard()
    }

    private fun getPhotos() {
        coroutineScope.launch {
            getGalleryImagesPath()
            this.cancel()
        }
    }

    private fun getVideos() {
        coroutineScope.launch {
            getGalleryVideoPath()
            this.cancel()
        }
    }

    private fun shouldInterceptBackPress(): Boolean {
        return bindingType.bottomSheetAttachmentTypeRoot.checkedRadioButtonId != -1
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAndEditEventBinding.inflate(inflater, container, false)
        bindingType = BottomSheetChooseAttachmentTypeBinding
            .bind(binding.root.findViewById(R.id.bottom_sheet_choose_attachment_type))
        bindingButton = BottomSheetButtonBinding
            .bind(binding.root.findViewById(R.id.bottom_sheet_button))
        bindingCover = BottomSheetAddBinding
            .bind(binding.root.findViewById(R.id.bottom_sheet_add_cover))
        bindingImage = BottomSheetAddBinding
            .bind(binding.root.findViewById(R.id.bottom_sheet_add_image))
        bindingVideo = BottomSheetAddBinding
            .bind(binding.root.findViewById(R.id.bottom_sheet_add_video))
        /*bindingAudio = BottomSheetChooseAttachmentTypeBinding.inflate(inflater)
        bindingFile = BottomSheetChooseAttachmentTypeBinding.inflate(inflater)*/
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        val coverAdapter = CoverAdapter(this)
        val imageAdapter = ImageAdapter(this)
        val videoAdapter = VideoAdapter(this)

        val attachmentAdapter = AttachmentAdapter()

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.create_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.create_menu_save -> {
                        viewModel.saveEvent()
                    }
                    R.id.create_menu_delete -> {
                        viewModel.deleteEvent()
                    }
                }
                return true
            }
        }

        actionModCallBack = object : Callback {

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                contextActionMode = null
                if (isNeedClose) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    videoAdapter.updateChosen(emptyList())
                    bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    imageAdapter.updateChosen(emptyList())
                    bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    coverAdapter.updateChosen(Uri.EMPTY)
                }
            }
        }

        attachmentBottomSheetInit()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (shouldInterceptBackPress()) {
                        isEnabled = true
                        contextActionMode?.finish()
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        /*bottomSheetAudioBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        bottomSheetFileBehavior.state = BottomSheetBehavior.STATE_HIDDEN*/
                    } else {
                        isEnabled = false
                        view.findNavController().navigateUp()
                    }
                }

            })

        menuHost.addMenuProvider(
            menuProvider,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackBar -> {
                        Snackbar.make(
                            view,
                            event.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is UiEvent.Save -> {
                        view.findNavController().navigateUp()
                    }
                    is UiEvent.Delete -> {
                        view.findNavController().navigateUp()
                    }
                    is UiEvent.UpdateUiState -> {
                        binding.createAndEditEventTitleEdt.setText(viewModel.currentEvent.value.title.text)
                    }
                    else -> {}
                }
            }

        }

        binding.apply {
            createAndEditEventTitleEdt.setOnClickListener {
                if (bottomSheetCoverBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                if (bottomSheetImageBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                if (bottomSheetVideoBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                /*bottomSheetAudioBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                bottomSheetFileBehavior.state = BottomSheetBehavior.STATE_HIDDEN*/
            }
            createAndEditEventAllDaySwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateAllDayState(isChecked)
            }
            createAndEditEventDeleteCoverButton.setOnClickListener {
                viewModel.updateCover(Uri.EMPTY)
                coverAdapter.updateChosen(Uri.EMPTY)
            }
            scrollView2.setOnScrollChangeListener { _, _, _, _, _ ->
                requireActivity().hideKeyboard()
                if (bottomSheetCoverBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                if (bottomSheetImageBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                if (bottomSheetVideoBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            createAndEditEventAttachmentContentRoot.apply {
                adapter = attachmentAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                addItemDecoration(HorizontalListsItemDecoration(10, 10))
            }
        }

        bindingCover.bottomSheetRv.apply {
            adapter = coverAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecoration(20, 2))
        }

        bindingImage.bottomSheetRv.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(GridItemDecoration(10, 3))
        }

        bindingVideo.bottomSheetRv.apply {
            adapter = videoAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(GridItemDecoration(10, 3))
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.apply {
                    coverAdapter.submitList(event.covers)
                    coverAdapter.updateChosen(event.chosenCover)
                    imageAdapter.submitList(event.images)
                    imageAdapter.updateChosen(event.chosenImages)
                    videoAdapter.submitList(event.videos)
                    videoAdapter.updateChosen(event.chosenVideos)
                    attachmentAdapter.submitList(event.attachments)

                    createAndEditEventTitleEdt.addTextChangedListener(
                        afterTextChanged = { text ->
                            viewModel.updateTitle(text.toString())
                        }
                    )

                    if (event.title.isError) {
                        createAndEditEventTitleError.visibility = View.VISIBLE
                    } else {
                        createAndEditEventTitleError.visibility = View.GONE
                    }
                    if (event.isStartDateError) {
                        createAndEditEventStartDateRoot.strokeWidth = 5
                    } else {
                        createAndEditEventStartDateRoot.strokeWidth = 0
                    }
                    if (event.isEndDateError) {
                        createAndEditEventEndDateRoot.strokeWidth = 5
                    } else {
                        createAndEditEventEndDateRoot.strokeWidth = 0
                    }
                    if (event.startDate != 0L || event.startTime != 0L) {
                        createAndEditEventStartDateDate.text = event.startDate.toDate()
                        createAndEditEventStartDateTime.text = event.startTime.toTime()
                        createAndEditEventStartDateTime.visibility = View.VISIBLE
                    }
                    if (event.endDate != 0L || event.endTime != 0L) {
                        createAndEditEventEndDateDate.text = event.endDate.toDate()
                        createAndEditEventEndDateTime.text = event.endTime.toTime()
                        createAndEditEventEndDateTime.visibility = View.VISIBLE
                    }
                    if (event.allDay) {
                        createAndEditEventEndDateTime.visibility = View.GONE
                        createAndEditEventStartDateTime.visibility = View.GONE
                        createAndEditEventEndDateAndTimeDivider.visibility = View.GONE
                        createAndEditEventStartDateAndTimeDivider.visibility = View.GONE
                    } else if (event.startDate != 0L && event.endDate != 0L) {
                        createAndEditEventEndDateTime.visibility = View.VISIBLE
                        createAndEditEventStartDateTime.visibility = View.VISIBLE
                        createAndEditEventEndDateAndTimeDivider.visibility = View.VISIBLE
                        createAndEditEventStartDateAndTimeDivider.visibility = View.VISIBLE
                    }
                    if (event.color != -1) {
                        createAndEditEventColor.setCardBackgroundColor(event.color)
                    }
                    if (event.chosenCover != Uri.EMPTY) {
                        scrollView2.smoothScrollTo(0, 0)
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(motionHead.id, View.VISIBLE)
                            constraintSet?.applyTo(motionBase)
                        }
                        Glide
                            .with(createAndEditEventCover.context)
                            .load(event.chosenCover)
                            .into(createAndEditEventCover)
                    } else {
                        createAndEditEventCover.setImageDrawable(null)
                        motionBase.constraintSetIds.forEach {
                            val constraintSet =
                                motionBase.getConstraintSet(it) ?: null
                            constraintSet?.setVisibility(motionHead.id, View.GONE)
                            constraintSet?.applyTo(motionBase)
                        }
                    }
                    createAndEditEventStartDateTime.setOnClickListener {
                        BottomSheetTimePickerFragment(
                            event.startTime,
                            REQUEST_KEY,
                            "startTime"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseTime"
                        )
                        createAndEditEventStartDateAndTimeDivider.visibility = View.VISIBLE
                    }
                    createAndEditEventEndDateTime.setOnClickListener {
                        BottomSheetTimePickerFragment(
                            event.endTime,
                            REQUEST_KEY,
                            "endTime"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseTime"
                        )
                        createAndEditEventEndDateAndTimeDivider.visibility = View.VISIBLE
                    }
                    createAndEditEventNote.text = event.note
                    createAndEditEventAllDaySwitch.isChecked = event.allDay
                    createAndEditEventRepeat.text = getString(event.repeatTitle)

                    var titleString = ""
                    event.notificationTitle.forEach { title ->
                        titleString += if (event.notificationTitle.size == 1)
                            getString(title)
                        else
                            getString(title) + "; "
                    }

                    createAndEditEventNotification.text = titleString

                    createAndEditEventStartDateDate.setOnClickListener {
                        BottomSheetDatePickerFragment(
                            event.startDate,
                            REQUEST_KEY,
                            "startDate"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
                        )
                    }

                    createAndEditEventEndDateDate.setOnClickListener {
                        BottomSheetDatePickerFragment(
                            event.endDate,
                            REQUEST_KEY,
                            "endDate"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetChooseDate"
                        )
                    }

                    createAndEditEventNoteRoot.setOnClickListener {
                        BottomSheetAddNoteFragment(event.note, REQUEST_KEY, "note").show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetNote"
                        )
                    }

                    createAndEditEventRepeatRoot.setOnClickListener {
                        BottomSheetChooseRepeatFragment(event.repeat, REQUEST_KEY, "repeat").show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetRepeat"
                        )
                    }

                    createAndEditEventNotificationRoot.setOnClickListener {
                        BottomSheetChooseNotificationFragment(
                            event.notifications,
                            REQUEST_KEY,
                            "notifications"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetNotification"
                        )
                    }

                    createAndEditEventColor.setOnClickListener {
                        BottomSheetChooseColorFragment(
                            event.color,
                            REQUEST_KEY,
                            "color"
                        ).show(
                            requireActivity().supportFragmentManager,
                            "BottomSheetColor"
                        )
                    }

                    createAndEditEventAttachmentRoot.setOnClickListener {
                        getPhotos()
                        if (requireActivity().isDarkTheme())
                            requireActivity().setNavigationColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_gray
                                )
                            )
                        bindingType.bottomSheetAttachmentTypeRoot.clearCheck()
                        bindingType.bottomSheetAttachmentTypeRoot.check(R.id.bottom_sheet_attachment_type_cover_rb)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }

        bindingType.apply {
            bottomSheetAttachmentTypeCoverRb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    bottomSheetCoverBehavior.apply {
                        peekHeight = 1300
                        state = BottomSheetBehavior.STATE_COLLAPSED
                        bindingCover.bottomSheetRv.scrollToPosition(0)
                        addBottomSheetCallback(
                            BottomSheetCallback(
                                onStateChange = { _, newState ->
                                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                        bindingImage.bottomSheetHandle.alpha = 0f
                                        bottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_HIDDEN
                                        bindingCover.root.background =
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.no_corners
                                            )
                                        if (contextActionMode == null) {
                                            isNeedClose = true
                                            contextActionMode = (requireActivity() as MainActivity)
                                                .startSupportActionMode(actionModCallBack)
                                        }
                                    } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                        bindingCover.root.background =
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.top_corners
                                            )
                                    } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        bindingCover.bottomSheetHandle.alpha = 1f
                                        if (bottomSheetButtonBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_EXPANDED
                                        }
                                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        if (bindingType.bottomSheetAttachmentTypeCoverRb.isChecked) {
                                            bindingType.bottomSheetAttachmentTypeRoot.clearCheck()
                                            bindingCover.root.background =
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.top_corners
                                                )
                                            if (requireActivity().isDarkTheme())
                                                requireActivity().setNavigationColor(
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        R.color.onyx
                                                    )
                                                )
                                        }
                                    }
                                },
                                onSlideSheet = { _, offset, prev ->
                                    if (offset > 0 && prev < offset) {
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                                        bindingCover.bottomSheetHandle.alpha = 1 - offset
                                    } else if (offset >= 0f && state == BottomSheetBehavior.STATE_DRAGGING) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        bottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_EXPANDED
                                        bindingCover.bottomSheetHandle.alpha = 1 - offset
                                    } else if (state == BottomSheetBehavior.STATE_DRAGGING) {
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                                        bottomSheetButtonBehavior.state =
                                            BottomSheetBehavior.STATE_HIDDEN
                                    }
                                }
                            )
                        )
                    }
                    bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            bottomSheetAttachmentTypeImageRb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    getPhotos()
                    bottomSheetImageBehavior.apply {
                        bindingCover.bottomSheetRv.scrollToPosition(0)
                        peekHeight = 1300
                        state = BottomSheetBehavior.STATE_COLLAPSED
                        addBottomSheetCallback(
                            BottomSheetCallback(
                                onStateChange = { _, newState ->
                                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                        bindingImage.bottomSheetHandle.alpha = 0f
                                        bottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_HIDDEN
                                        bindingImage.root.background =
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.no_corners
                                            )
                                        if (contextActionMode == null) {
                                            isNeedClose = true
                                            contextActionMode = (requireActivity() as MainActivity)
                                                .startSupportActionMode(actionModCallBack)
                                        }
                                    } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                        bindingImage.root.background =
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.top_corners
                                            )
                                    } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        bindingImage.bottomSheetHandle.alpha = 1f
                                        if (bottomSheetButtonBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_EXPANDED
                                        }
                                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        if (bindingType.bottomSheetAttachmentTypeImageRb.isChecked) {
                                            bindingType.bottomSheetAttachmentTypeRoot.clearCheck()
                                            bindingImage.root.background =
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.top_corners
                                                )
                                            if (requireActivity().isDarkTheme())
                                                requireActivity().setNavigationColor(
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        R.color.onyx
                                                    )
                                                )
                                        }
                                    }
                                },
                                onSlideSheet = { _, offset, prev ->
                                    if (offset > 0 && prev < offset) {
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                                        bindingImage.bottomSheetHandle.alpha = 1 - offset
                                    } else if (offset >= 0f && state == BottomSheetBehavior.STATE_DRAGGING) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        if (bottomSheetButtonBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_EXPANDED
                                        }
                                        bindingImage.bottomSheetHandle.alpha = 1 - offset
                                    } else if (state == BottomSheetBehavior.STATE_DRAGGING) {
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                                        bottomSheetButtonBehavior.state =
                                            BottomSheetBehavior.STATE_HIDDEN
                                    }
                                }
                            )
                        )
                    }
                    bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            bottomSheetAttachmentTypeVideoRb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    getVideos()
                    bottomSheetVideoBehavior.apply {
                        bindingCover.bottomSheetRv.scrollToPosition(0)
                        peekHeight = 1300
                        state = BottomSheetBehavior.STATE_COLLAPSED
                        addBottomSheetCallback(
                            BottomSheetCallback(
                                onStateChange = { _, newState ->
                                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                        bindingImage.bottomSheetHandle.alpha = 0f
                                        bottomSheetBehavior.state =
                                            BottomSheetBehavior.STATE_HIDDEN
                                        bindingVideo.root.background =
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.no_corners
                                            )
                                        if (contextActionMode == null) {
                                            isNeedClose = true
                                            contextActionMode = (requireActivity() as MainActivity)
                                                .startSupportActionMode(actionModCallBack)
                                        }
                                    } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                                        bindingVideo.bottomSheetHandle.alpha = 1f
                                        if (bottomSheetButtonBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_EXPANDED
                                        }
                                    } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        bindingVideo.root.background =
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.top_corners
                                            )
                                    } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        if (bindingType.bottomSheetAttachmentTypeVideoRb.isChecked) {
                                            bindingType.bottomSheetAttachmentTypeRoot.clearCheck()
                                            bindingVideo.root.background =
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.top_corners
                                                )
                                            if (requireActivity().isDarkTheme())
                                                requireActivity().setNavigationColor(
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        R.color.onyx
                                                    )
                                                )
                                        }
                                    }
                                },
                                onSlideSheet = { _, offset, prev ->
                                    if (offset > 0 && prev < offset) {
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                                        bindingVideo.bottomSheetHandle.alpha = 1 - offset
                                    } else if (offset >= 0f && state == BottomSheetBehavior.STATE_DRAGGING) {
                                        isNeedClose = false
                                        contextActionMode?.finish()
                                        if (bottomSheetButtonBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_EXPANDED
                                        }
                                        bindingVideo.bottomSheetHandle.alpha = 1 - offset
                                    } else if (state == BottomSheetBehavior.STATE_DRAGGING) {
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                                        bottomSheetButtonBehavior.state =
                                            BottomSheetBehavior.STATE_HIDDEN
                                    }
                                }
                            )
                        )
                    }
                    bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }

    }

    private fun attachmentBottomSheetInit() {
        bottomSheetBehavior = BottomSheetBehavior
            .from(bindingType.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetButtonBehavior = BottomSheetBehavior
            .from(bindingButton.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetCoverBehavior = BottomSheetBehavior
            .from(bindingCover.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bindingCover.bottomSheetTitle.text =
            getString(R.string.bottom_sheet_choose_cover_title)

        bottomSheetImageBehavior = BottomSheetBehavior
            .from(bindingImage.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }


        bindingImage.bottomSheetTitle.text =
            getString(R.string.bottom_sheet_choose_image_title)

        bottomSheetVideoBehavior = BottomSheetBehavior
            .from(bindingVideo.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bindingVideo.bottomSheetTitle.text =
            getString(R.string.bottom_sheet_choose_video_title)

        /*bottomSheetAudioBehavior = BottomSheetBehavior
            .from(bindingAudio.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

            bindingAudio.bottomSheetTitle.text =
            getString(R.string.bottom_sheet_choose_audio_title)

        bottomSheetFileBehavior = BottomSheetBehavior
            .from(bindingFile.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

            bindingFile.bottomSheetTitle.text =
            getString(R.string.bottom_sheet_choose_file_title)
            */

        binding.bottomSheetButton.bottomSheetChooseButton.setOnClickListener {
            viewModel.updateAttachment()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetCoverBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetImageBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetVideoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!requireActivity().checkPermission(READ_MEDIA_IMAGES)) {
                bindingImage.bottomSheetPermissionMessage.visibility = View.VISIBLE
                bindingImage.bottomSheetPermissionMessage.setOnClickListener {
                    activityResultLauncher.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
                }
            }
            if (!requireActivity().checkPermission(READ_MEDIA_VIDEO)) {
                bindingVideo.bottomSheetPermissionMessage.visibility = View.VISIBLE
                bindingVideo.bottomSheetPermissionMessage.setOnClickListener {
                    activityResultLauncher.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
                }
            }
        } else {
            if (!requireActivity().checkPermission(READ_EXTERNAL_STORAGE)) {
                bindingImage.bottomSheetPermissionMessage.visibility = View.VISIBLE
                bindingVideo.bottomSheetPermissionMessage.visibility = View.VISIBLE
                bindingImage.bottomSheetPermissionMessage.setOnClickListener {
                    activityResultLauncher.launch(arrayOf(READ_EXTERNAL_STORAGE))
                }
                bindingVideo.bottomSheetPermissionMessage.setOnClickListener {
                    activityResultLauncher.launch(arrayOf(READ_EXTERNAL_STORAGE))
                }
            }
        }
    }

    private fun showAppSettingsScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private suspend fun getGalleryImagesPath() = coroutineScope {
        launch {
            val imagePathList = mutableListOf<Uri>()
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns._ID)
            val sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
            val cursor: Cursor? = requireContext()
                .contentResolver
                .query(uri, projection, null, null, sortOrder)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id: Int =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    imagePathList.add(ContentUris.withAppendedId(uri, id.toLong()))
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
            val projection =
                arrayOf(
                    MediaStore.MediaColumns._ID,
                    MediaStore.Video.VideoColumns.DURATION,
                    MediaStore.Video.VideoColumns.SIZE
                )
            val sortOrder = MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC"
            val cursor: Cursor? = requireContext()
                .contentResolver
                .query(uri, projection, null, null, sortOrder)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id: Int = cursor
                        .getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val duration = cursor
                        .getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))
                    val size = cursor
                        .getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE))
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

    override fun onCoverChosen(uri: Uri) {
        viewModel.updateCover(uri)
    }

    override fun onImagesChosen(items: List<Uri>) {
        val prevState = viewModel.currentEvent.value.chosenImages
        if (items.isNotEmpty() || items != prevState) {
            bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            if (bottomSheetImageBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.updateChosenImage(items)
    }

    override fun onVideosChosen(items: List<VideoItemState>) {
        val prevState = viewModel.currentEvent.value.chosenVideos
        if (items.isNotEmpty() || items != prevState) {
            bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            bottomSheetButtonBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            if (bottomSheetVideoBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.updateChosenVideo(items)
    }

    override fun onAudioChosen(uri: Uri) {

    }

    override fun onFileChosen(uri: Uri) {

    }

    companion object {
        const val REQUEST_KEY = "createEvent"
    }
}
