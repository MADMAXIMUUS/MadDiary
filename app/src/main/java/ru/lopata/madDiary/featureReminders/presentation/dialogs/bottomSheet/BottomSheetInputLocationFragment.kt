package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet

import android.app.Dialog
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.lopata.madDiary.databinding.FragmentBottomSheetInputLocationBinding

class BottomSheetInputLocationFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetInputLocationBinding? = null
    private val binding get() = _binding!!

    private var expandedHeight = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetInputLocationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface -> setupRatio(dialogInterface as BottomSheetDialog) }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return

        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        val bottomSheetLayoutParams = bottomSheet.layoutParams
        expandedHeight = getBottomSheetDialogDefaultHeight()
        bottomSheetLayoutParams.height = (expandedHeight * 0.935).toInt()
        BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
        BottomSheetBehavior.from(bottomSheet).maxHeight = (expandedHeight * 0.935).toInt()
        BottomSheetBehavior.from(bottomSheet).peekHeight = (expandedHeight * 0.935).toInt()
        bottomSheet.layoutParams = bottomSheetLayoutParams
        BottomSheetBehavior.from(bottomSheet).isHideable = true
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
}