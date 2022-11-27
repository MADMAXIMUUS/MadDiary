package ru.lopata.madDiary.featureReminders.presentation.dialogs.modal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentSelectedDateEventsDialogBinding
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarDayState
import java.util.*

@AndroidEntryPoint
class SelectedDateEventsDialogFragment(
    private val dayDetail: CalendarDayState
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
            "${dayDetail.day.get(Calendar.DAY_OF_MONTH)} " +
                    "${dayDetail.day.get(Calendar.MONTH)}" +
                    " ${dayDetail.day.get(Calendar.YEAR)}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}