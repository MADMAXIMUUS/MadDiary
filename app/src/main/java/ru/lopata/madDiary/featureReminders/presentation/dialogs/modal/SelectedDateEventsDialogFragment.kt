package ru.lopata.madDiary.featureReminders.presentation.dialogs.modal

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.FileItemDecoration
import ru.lopata.madDiary.core.util.toCalendarDate
import ru.lopata.madDiary.databinding.FragmentSelectedDateEventsDialogBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.EventInCalendarGrid
import ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.adapter.CalendarEventDialogAdapter

@AndroidEntryPoint
class SelectedDateEventsDialogFragment(
    private val listener: OnEventClickListener,
    private val day: Calendar,
    private val events: List<EventInCalendarGrid>
) : DialogFragment(), CalendarEventDialogAdapter.OnEventClickListener {

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

        val eventAdapter = CalendarEventDialogAdapter(this)
        eventAdapter.submitList(events)

        binding.selectedDateEventsDialogEvents.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(FileItemDecoration(20, 20))
        }

        binding.selectedDateEventsDialogNewButton.setOnClickListener {
            dismiss()
            listener.onNewClick(day.timeInMillis)
        }

        binding.selectedDateEventsDialogDate.text = day.timeInMillis.toCalendarDate()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(eventId: Int, type: Event.Types) {
        dismiss()
        listener.onClick(eventId, type)
    }

    override fun onCheckClick(eventId: Int, state: Boolean) {
        listener.onCheckClick(eventId, state)
    }

    interface OnEventClickListener {
        fun onClick(eventId: Int, type: Event.Types)
        fun onCheckClick(eventId: Int, state: Boolean)
        fun onNewClick(date: Long)
    }
}