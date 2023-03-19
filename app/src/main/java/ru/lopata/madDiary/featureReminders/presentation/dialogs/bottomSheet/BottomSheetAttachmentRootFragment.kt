package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.google.android.material.R.id.design_bottom_sheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.isDarkTheme
import ru.lopata.madDiary.core.util.setStatusBarColor
import ru.lopata.madDiary.databinding.FragmentBottomSheetAttachmentRootBinding
import ru.lopata.madDiary.featureReminders.domain.model.states.AudioItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.FileItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.ImageItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.VideoItemState
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.*
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters.OnAttachmentDialogListener
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.AudioPreviewDialog
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.ImagePreviewDialog
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.VideoPreviewDialog
import ru.lopata.madDiary.featureReminders.util.BottomSheetCallback

class BottomSheetAttachmentRootFragment private constructor(
    private val covers: List<Uri>,
    private val images: List<ImageItemState>,
    private val videos: List<VideoItemState>,
    private val audios: List<AudioItemState>,
    private val files: List<FileItemState>,
    private val chosenCover: Uri,
    private var chosenImages: List<ImageItemState>,
    private var chosenVideos: List<VideoItemState>,
    private val listener: OnAttachmentDialogListener?
) : BottomSheetDialogFragment(), OnAttachmentDialogListener {

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

    private lateinit var coverFragment: AttachCoverFragment
    private lateinit var imageFragment: AttachImageFragment
    private lateinit var videoFragment: AttachVideoFragment
    private lateinit var audioFragment: AttachAudioFragment
    private lateinit var fileFragment: AttachFileFragment

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
        coverFragment = AttachCoverFragment(covers, chosenCover, this)
        imageFragment = AttachImageFragment(images, chosenImages, this)
        videoFragment = AttachVideoFragment(videos, chosenVideos, this)
        audioFragment = AttachAudioFragment(audios, this)
        fileFragment = AttachFileFragment(files, this)
        var transaction = fragmentManager.beginTransaction()
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.bottom_sheet_attachment_nav_host, coverFragment)
        }
        transaction.commit()

        binding.bottomSheetCloseButton.setOnClickListener {
            if (binding.bottomSheetAttachmentStickyConfirm.visibility != View.VISIBLE) {
                dismiss()
            }
        }

        binding.bottomSheetAttachmentTypeRoot.check(R.id.bottom_sheet_attachment_type_cover_rb)

        binding.bottomSheetAttachmentTypeCoverRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transaction = fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, coverFragment)
                }
                transaction.commit()
            }
        }
        binding.bottomSheetAttachmentTypeImageRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transaction = fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, imageFragment)
                }
                binding.bottomSheetConfirmButton.setOnClickListener {
                    listener?.onImagesChosen(chosenImages)

                    dismiss()
                }
                transaction.commit()
            }
        }
        binding.bottomSheetAttachmentTypeVideoRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transaction = fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, videoFragment)
                }
                binding.bottomSheetConfirmButton.setOnClickListener {
                    listener?.onVideosChosen(chosenVideos)
                    dismiss()
                }
                transaction.commit()
            }
        }
        binding.bottomSheetAttachmentTypeAudioRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transaction = fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, audioFragment)
                }
                transaction.commit()
            }
        }
        binding.bottomSheetAttachmentTypeFileRb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                transaction = fragmentManager.beginTransaction()
                fragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        androidx.navigation.ui.R.anim.nav_default_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_exit_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_enter_anim,
                        androidx.navigation.ui.R.anim.nav_default_pop_exit_anim,
                    )
                    replace(R.id.bottom_sheet_attachment_nav_host, fileFragment)
                }
                transaction.commit()
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
                        binding.bottomSheetCloseButton.visibility = View.VISIBLE
                        if (requireActivity().isDarkTheme())
                            requireActivity().setStatusBarColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_gray
                                )
                            )
                    } else {
                        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        dialog.behavior.maxHeight = (expandedHeight * 0.915).toInt()
                        binding.bottomSheetCloseButton.visibility = View.GONE
                        if (requireActivity().isDarkTheme())
                            requireActivity().setStatusBarColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.onyx
                                )
                            )
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
        BottomSheetBehavior.from(bottomSheet).maxHeight = (expandedHeight * 0.915).toInt()
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
            windowMetrics.bounds.height() - insets.bottom
        } else {
            val window = requireActivity().window
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            rect.height() - rect.top
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

    override fun onImageChosen(item: ImageItemState, state: Boolean) {
        val list = imageFragment.updateChosen(item, state)
        if (list.isNotEmpty() || list != chosenImages) {
            binding.bottomSheetAttachmentSticky.visibility = View.GONE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.VISIBLE
            chosenImages = list
        } else {
            binding.bottomSheetAttachmentSticky.visibility = View.VISIBLE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.INVISIBLE
        }
        onImagesChosen(list)
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

    override fun onVideoChosen(item: VideoItemState, state: Boolean) {
        val list = videoFragment.updateChosen(item, state)
        if (list.isNotEmpty() || list != chosenVideos) {
            binding.bottomSheetAttachmentSticky.visibility = View.GONE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.VISIBLE
            chosenVideos = list
        } else {
            binding.bottomSheetAttachmentSticky.visibility = View.VISIBLE
            binding.bottomSheetAttachmentStickyConfirm.visibility = View.INVISIBLE
        }
        onVideosChosen(list)
    }

    override fun onAudioChosen(item: AudioItemState) {
        listener?.onAudioChosen(item)
        dismiss()
    }

    override fun onFileChosen(item: FileItemState) {
        listener?.onFileChosen(item)
        dismiss()
    }

    override fun onImageDialogShow(item: ImageItemState, isChosen: Boolean) {
        ImagePreviewDialog(item, true, isChosen, this).show(
            requireActivity().supportFragmentManager,
            "ImagePreviewDialog"
        )
    }

    override fun onVideoDialogShow(item: VideoItemState, isChosen: Boolean) {
        VideoPreviewDialog(item, true, isChosen, this).show(
            requireActivity().supportFragmentManager,
            "VideoPreviewDialog"
        )
    }

    override fun onAudioDialogShow(item: AudioItemState, isChosen: Boolean) {
        AudioPreviewDialog(item, true, isChosen, this).show(
            requireActivity().supportFragmentManager,
            "ImagePreviewDialog"
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (requireActivity().isDarkTheme()) {
            requireActivity().setStatusBarColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.onyx
                )
            )
        }
    }

    data class Builder(
        private var covers: List<Uri> = emptyList(),
        private var images: List<ImageItemState> = emptyList(),
        private var videos: List<VideoItemState> = emptyList(),
        private var audios: List<AudioItemState> = emptyList(),
        private var files: List<FileItemState> = emptyList(),
        private var chosenCover: Uri = Uri.EMPTY,
        private var chosenImages: List<ImageItemState> = emptyList(),
        private var chosenVideos: List<VideoItemState> = emptyList(),
        private var listener: OnAttachmentDialogListener? = null
    ) {
        fun covers(covers: List<Uri>) = apply { this.covers = covers }
        fun images(images: List<ImageItemState>) = apply { this.images = images }
        fun videos(videos: List<VideoItemState>) = apply { this.videos = videos }
        fun audios(audios: List<AudioItemState>) = apply { this.audios = audios }
        fun files(files: List<FileItemState>) = apply { this.files = files }
        fun chosenCover(cover: Uri) = apply { this.chosenCover = cover }
        fun chosenImages(images: List<ImageItemState>) = apply { this.chosenImages = images }
        fun chosenVideos(videos: List<VideoItemState>) = apply { this.chosenVideos = videos }
        fun onAttachmentChosenListener(listener: OnAttachmentDialogListener) =
            apply { this.listener = listener }

        fun build() = BottomSheetAttachmentRootFragment(
            covers,
            images,
            videos,
            audios,
            files,
            chosenCover,
            chosenImages,
            chosenVideos,
            listener
        )
    }
}