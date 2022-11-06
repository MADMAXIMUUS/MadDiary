package ru.lopata.madDiary.featureReminders.presentation.viewEvent

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.*
import ru.lopata.madDiary.databinding.FragmentViewEventBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event

@AndroidEntryPoint
class ViewEventFragment : Fragment() {

    private var _binding: FragmentViewEventBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        _binding = FragmentViewEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.view_event_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.view_menu_edit -> {
                        viewModel.editEvent()
                    }
                    R.id.view_menu_delete -> {
                        viewModel.deleteEvent()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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
                    UiEvent.Delete -> {
                        view.findNavController().navigateUp()
                    }
                    is UiEvent.Edit -> {
                        view.findNavController().navigate(
                            R.id.createReminderFragment, event.passObject, NavOptions.Builder()
                                .setPopUpTo(R.id.viewEventFragment, true)
                                .build()
                        )
                    }
                    else -> {}
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentEvent.collectLatest { event ->
                binding.apply {
                    viewEventTitle.text = "${event.title}(${event.chapter}/${event.chapters})"
                    viewEventStartDateDate.text = event.startDateTime.time.toDate()
                    viewEventStartDateTime.text = event.startDateTime.time.toTime()
                    viewEventStartDateAndTimeDivider.visibility = View.VISIBLE
                    viewEventEndDateDate.text = event.endDateTime.time.toDate()
                    viewEventEndDateTime.text = event.endDateTime.time.toTime()
                    viewEventEndDateAndTimeDivider.visibility = View.VISIBLE
                    viewEventNote.text = event.note
                    viewEventRepeat.text = resources.getString(event.repeat) + " " +
                            event.repeatEnd.time.toDateTime()
                    viewEventAttachmentRoot.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}