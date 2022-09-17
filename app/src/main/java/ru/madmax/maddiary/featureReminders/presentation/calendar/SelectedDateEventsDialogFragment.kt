package ru.madmax.madDiary.featureReminders.presentation.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.madDiary.R
import ru.madmax.madDiary.core.util.monthYear
import ru.madmax.madDiary.databinding.FragmentSelectedDateEventsDialogBinding
import ru.madmax.madDiary.featureReminders.domain.model.CalendarModel

@AndroidEntryPoint
class SelectedDateEventsDialogFragment(
    private val dayDetail: CalendarModel
) : DialogFragment() {

    private var _binding: FragmentSelectedDateEventsDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme_transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedDateEventsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectedDateEventsDialogDate.text =
            "${dayDetail.day.dayOfMonth} ${dayDetail.day.monthYear()}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}