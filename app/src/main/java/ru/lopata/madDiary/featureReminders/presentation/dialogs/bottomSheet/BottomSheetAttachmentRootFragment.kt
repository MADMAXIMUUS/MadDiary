package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet

import android.app.Dialog
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.R.id.design_bottom_sheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentBottomSheetAttachmentRootBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters.OnAttachmentChosenListener
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.AudioItemState
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.FileItemState
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.ImageItemState
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.VideoItemState
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.AttachCoverFragment
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.AttachFileFragment
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.AttachImageFragment
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.AttachVideoFragment
import ru.lopata.madDiary.featureReminders.util.BottomSheetCallback

open class BottomSheetAttachmentRootFragment private constructor(
    private val covers: List<Uri>,
    private val images: List<ImageItemState>,
    private val videos: List<VideoItemState>,
    private val files: List<FileItemState>,
    private val chosenCover: Uri,
    private var chosenImages: List<ImageItemState>,
    private var chosenVideos: List<VideoItemState>,
    private val listener: OnAttachmentChosenListener?
) : BottomSheetDialogFragment(), OnAttachmentChosenListener {

    private var _binding: FragmentBottomSheetAttachmentRootBinding? = null
    private val binding get() = _binding!!

    private lateinit var typeLayoutParams: ConstraintLayout.LayoutParams
    private lateinit var confirmButtonLayoutParams: ConstraintLayout.LayoutParams
    private var collapsedMargin = 0
    private var collapsedConfirmMargin = 0
    private var buttonHeight = 0
    private var buttonConfirmHeight = 0
    private var expandedHeight = 0
    private var topMarginConfirm = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetAttachmentRootBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager = childFragmentManager
        var fragment: Fragment = AttachCoverFragment(covers, chosenCover, this)
        fragmentManager.beginTransaction()
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.bottom_sheet_attachment_nav_host, fragment)
        }

        binding.mainToolbar.setNavigationIcon(R.drawable.ic_close_dark)
        binding.mainToolbar.setNavigationOnClickListener {
            dismiss()
        }

        binding.bottomSheetAttachmentTypeRoot.check(R.id.bottom_sheet_attachment_type_cover_rb)

        binding.bottomSheetAttachmentTypeCoverRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fragment = AttachCoverFragment(covers, chosenCover, this)
                fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, fragment)
                }
            }
        }
        binding.bottomSheetAttachmentTypeImageRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fragment = AttachImageFragment(images, chosenImages, this)
                fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, fragment)
                }
                binding.bottomSheetConfirmButton.setOnClickListener {
                    listener?.onImagesChosen(chosenImages)
                    dismiss()
                }
            }
        }
        binding.bottomSheetAttachmentTypeVideoRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fragment = AttachVideoFragment(videos, chosenVideos, this)
                fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, fragment)
                }
                binding.bottomSheetConfirmButton.setOnClickListener {
                    listener?.onVideosChosen(chosenVideos)
                    dismiss()
                }
            }
        }
        binding.bottomSheetAttachmentTypeFileRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fragment = AttachFileFragment(files, this)
                fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, fragment)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface -> setupRatio(dialogInterface as BottomSheetDialog) }
        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(
            BottomSheetCallback(
                onStateChange = { _, newState ->
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        dialog.behavior.maxHeight = expandedHeight
                        confirmButtonLayoutParams.topMargin =
                            (((dialog.behavior.maxHeight - buttonConfirmHeight) - collapsedConfirmMargin) + collapsedConfirmMargin)
                        binding.toolbarRoot.visibility = View.VISIBLE
                    } else {
                        dialog.behavior.maxHeight = (expandedHeight * 0.935).toInt()
                        binding.toolbarRoot.visibility = View.GONE
                        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    }
                },
                onSlideSheet = { _, slideOffset, _ ->
                    binding.bottomSheetHandle.alpha = 1 - slideOffset
                    if (slideOffset > 0) {
                        typeLayoutParams.topMargin =
                            ((expandedHeight - collapsedMargin) * slideOffset + collapsedMargin).toInt()
                        topMarginConfirm =
                            (((dialog.behavior.maxHeight - buttonConfirmHeight) - collapsedConfirmMargin) * slideOffset + collapsedConfirmMargin).toInt()
                        confirmButtonLayoutParams.topMargin = topMarginConfirm

                    } else {
                        typeLayoutParams.topMargin = collapsedMargin
                        confirmButtonLayoutParams.topMargin = collapsedConfirmMargin
                    }
                    binding.bottomSheetAttachmentSticky
                        .layoutParams = typeLayoutParams
                    binding.bottomSheetAttachmentStickyConfirm
                        .layoutParams = confirmButtonLayoutParams
                }
            )
        )
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(design_bottom_sheet)
            ?: return

        typeLayoutParams =
            binding.bottomSheetAttachmentSticky.layoutParams as ConstraintLayout.LayoutParams

        confirmButtonLayoutParams =
            binding.bottomSheetAttachmentStickyConfirm.layoutParams as ConstraintLayout.LayoutParams

        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        val bottomSheetLayoutParams = bottomSheet.layoutParams
        bottomSheetLayoutParams.height = getBottomSheetDialogDefaultHeight()
        expandedHeight = bottomSheetLayoutParams.height
        val peekHeight = (expandedHeight * 0.6).toInt()

        bottomSheet.layoutParams = bottomSheetLayoutParams
        BottomSheetBehavior.from(bottomSheet).skipCollapsed = false
        BottomSheetBehavior.from(bottomSheet).peekHeight = peekHeight
        BottomSheetBehavior.from(bottomSheet).maxHeight = (expandedHeight * 0.935).toInt()
        BottomSheetBehavior.from(bottomSheet).isHideable = true

        buttonHeight =
            binding.bottomSheetAttachmentSticky.height
        buttonConfirmHeight =
            binding.bottomSheetAttachmentStickyConfirm.height
        collapsedMargin = peekHeight - buttonHeight
        collapsedConfirmMargin = peekHeight - buttonConfirmHeight
        typeLayoutParams.topMargin = collapsedMargin
        confirmButtonLayoutParams.topMargin = collapsedConfirmMargin
        binding.bottomSheetAttachmentSticky.layoutParams = typeLayoutParams
        binding.bottomSheetAttachmentStickyConfirm.layoutParams = confirmButtonLayoutParams
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight()
    }

    private fun getWindowHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val window = requireActivity().window
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            rect.height()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPermissionButtonClick() {
        listener?.onPermissionButtonClick()
        dismiss()
    }

    override fun onInternalButtonClick() {
        listener?.onInternalButtonClick()
        dismiss()
    }

    override fun onCoverChosen(uri: Uri) {
        listener?.onCoverChosen(uri)
    }

    override fun onImagesChosen(items: List<ImageItemState>) {
        if (items.isNotEmpty() || items != chosenImages) {
            binding.bottomSheetAttachmentSticky.visibility = View.GONE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.VISIBLE
            chosenImages = items
        } else {
            binding.bottomSheetAttachmentSticky.visibility = View.VISIBLE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.INVISIBLE
        }
    }

    override fun onVideosChosen(items: List<VideoItemState>) {
        if (items.isNotEmpty() || items != chosenVideos) {
            binding.bottomSheetAttachmentSticky.visibility = View.GONE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.VISIBLE
            chosenVideos = items
        } else {
            binding.bottomSheetAttachmentSticky.visibility = View.VISIBLE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.INVISIBLE
        }
    }

    override fun onAudioChosen(item: AudioItemState) {
        listener?.onAudioChosen(item)
    }

    override fun onFileChosen(item: FileItemState) {
        listener?.onFileChosen(item)
    }

    data class Builder(
        private var covers: List<Uri> = emptyList(),
        private var images: List<ImageItemState> = emptyList(),
        private var videos: List<VideoItemState> = emptyList(),
        private var files: List<FileItemState> = emptyList(),
        private var chosenCover: Uri = Uri.EMPTY,
        private var chosenImages: List<ImageItemState> = emptyList(),
        private var chosenVideos: List<VideoItemState> = emptyList(),
        private var listener: OnAttachmentChosenListener? = null
    ) {
        fun covers(covers: List<Uri>) = apply { this.covers = covers }
        fun images(images: List<ImageItemState>) = apply { this.images = images }
        fun videos(videos: List<VideoItemState>) = apply { this.videos = videos }
        fun files(files: List<FileItemState>) = apply { this.files = files }
        fun chosenCover(cover: Uri) = apply { this.chosenCover = cover }
        fun chosenImages(images: List<ImageItemState>) = apply { this.chosenImages = images }
        fun chosenVideos(videos: List<VideoItemState>) = apply { this.chosenVideos = videos }
        fun onAttachmentChosenListener(listener: OnAttachmentChosenListener) =
            apply { this.listener = listener }

        fun build() = BottomSheetAttachmentRootFragment(
            covers,
            images,
            videos,
            files,
            chosenCover,
            chosenImages,
            chosenVideos,
            listener
        )
    }
}