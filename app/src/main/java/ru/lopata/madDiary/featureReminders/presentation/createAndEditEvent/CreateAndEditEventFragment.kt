package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import android.net.Uri
import android.os.Bundle
import android.view.*
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
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.databinding.BottomSheetAddCoverBinding
import ru.lopata.madDiary.databinding.BottomSheetChooseAttachmentTypeBinding
import ru.lopata.madDiary.databinding.FragmentCreateAndEditEventBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.CoverAdapter
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet.*
import ru.lopata.madDiary.featureReminders.util.BottomSheetCallback

@AndroidEntryPoint
class CreateAndEditEventFragment : Fragment(), CoverAdapter.OnItemClickListener {

    private var _binding: FragmentCreateAndEditEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAndEditEventViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingType: BottomSheetChooseAttachmentTypeBinding
    private lateinit var bottomSheetCoverBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bindingCover: BottomSheetAddCoverBinding
    private lateinit var bottomSheetImageBehavior: BottomSheetBehavior<View>
    private lateinit var bindingImage: BottomSheetAddCoverBinding
    private lateinit var bottomSheetVideoBehavior: BottomSheetBehavior<View>
    private lateinit var bindingVideo: BottomSheetAddCoverBinding
    private lateinit var bottomSheetAudioBehavior: BottomSheetBehavior<View>
    private lateinit var bindingAudio: BottomSheetAddCoverBinding
    private lateinit var bottomSheetFileBehavior: BottomSheetBehavior<View>
    private lateinit var bindingFile: BottomSheetAddCoverBinding

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (requireActivity().isDarkTheme()) {
            requireActivity().setNavigationColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.onyx
                )
            )
        }
        _binding = FragmentCreateAndEditEventBinding.inflate(inflater, container, false)
        bindingType = BottomSheetChooseAttachmentTypeBinding
            .bind(binding.root.findViewById(R.id.bottom_sheet_choose_attachment_type))
        bindingCover = BottomSheetAddCoverBinding
            .bind(binding.root.findViewById(R.id.bottom_sheet_add_cover))
        /*bindingImage = BottomSheetChooseAttachmentTypeBinding.inflate(inflater)
        bindingVideo = BottomSheetChooseAttachmentTypeBinding.inflate(inflater)
        bindingAudio = BottomSheetChooseAttachmentTypeBinding.inflate(inflater)
        bindingFile = BottomSheetChooseAttachmentTypeBinding.inflate(inflater)*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        attachmentBottomSheetInit()

        menuHost.addMenuProvider(
            object : MenuProvider {
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
            }, viewLifecycleOwner, Lifecycle.State.RESUMED
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
                    else -> {}
                }
            }

        }

        binding.apply {
            createAndEditEventTitleEdt.addTextChangedListener(
                onTextChanged = { text, _, _, _ ->
                    viewModel.updateTitle(text.toString())
                }
            )
            createAndEditEventAllDaySwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateAllDayState(isChecked)
            }
            createAndEditEventDeleteCoverButton.setOnClickListener {
                viewModel.updateCover(Uri.EMPTY)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.createAndEditEventTitleEdt.setText(event.title.text)
                this.cancel()
            }
        }

        val coverAdapter = CoverAdapter(this)

        bindingCover.bottomSheetCoverRv.apply {
            adapter = coverAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecoration(20, 2))
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.apply {
                    coverAdapter.submitList(event.covers)
                    coverAdapter.updateChosen(event.chosenCover)
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
                        if (requireActivity().isDarkTheme())
                            requireActivity().setNavigationColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_gray
                                )
                            )
                        bottomSheetBehavior.apply {
                            state = BottomSheetBehavior.STATE_EXPANDED
                            /*addBottomSheetCallback(
                                BottomSheetCallback(
                                    onStateChange = { _, newState ->
                                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                            if (requireActivity().isDarkTheme())
                                                requireActivity().setNavigationColor(
                                                    ContextCompat.getColor(
                                                        requireContext(),
                                                        R.color.onyx
                                                    )
                                                )
                                        }
                                    }
                                )
                            )*/
                        }
                        bottomSheetCoverBehavior.apply {
                            peekHeight = 1300
                            state = BottomSheetBehavior.STATE_COLLAPSED
                            addBottomSheetCallback(
                                BottomSheetCallback(
                                    onStateChange = { _, newState ->
                                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                            bindingCover.root.background =
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.no_corners
                                                )
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_HIDDEN
                                            bindingCover.bottomSheetCoverHandle.visibility =
                                                View.INVISIBLE
                                        }
                                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                            bindingCover.root.background =
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.top_corners
                                                )
                                            bindingCover.bottomSheetCoverHandle.visibility =
                                                View.VISIBLE
                                            bottomSheetBehavior.state =
                                                BottomSheetBehavior.STATE_EXPANDED
                                        }
                                    }
                                )
                            )
                        }
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
                    }
                } else {
                    bottomSheetCoverBehavior.apply {
                        peekHeight = 0
                    }
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

        bottomSheetCoverBehavior = BottomSheetBehavior
            .from(bindingCover.root)
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetImageBehavior = BottomSheetBehavior
            .from(binding.root.findViewById(R.id.bottom_sheet_add_image))
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetVideoBehavior = BottomSheetBehavior
            .from(binding.root.findViewById(R.id.bottom_sheet_add_video))
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetAudioBehavior = BottomSheetBehavior
            .from(binding.root.findViewById(R.id.bottom_sheet_add_audio))
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetFileBehavior = BottomSheetBehavior
            .from(binding.root.findViewById(R.id.bottom_sheet_add_file))
            .apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY = "createEvent"
    }

    override fun onItemClick(uri: Uri) {
        viewModel.updateCover(uri)
    }

}