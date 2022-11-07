package ru.lopata.madDiary.featureReminders.presentation.listEvents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.core.util.ListsItemDecoration
import ru.lopata.madDiary.databinding.FragmentListEventBinding
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet.BottomSheetChooseReminderTypeFragment

@AndroidEntryPoint
class ListEventFragment : Fragment(), ListEventAdapter.OnItemClickListener {

    private var _binding: FragmentListEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel: ListEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        val eventAdapter = ListEventAdapter(this)

        binding.eventListRv.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListsItemDecoration(10, 10))
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest { state ->
                eventAdapter.submitList(state.events)
            }
        }

        binding.eventListBtnCreateReminder.setOnClickListener {
            BottomSheetChooseReminderTypeFragment(navController).show(
                requireActivity().supportFragmentManager,
                "ChooseReminderType"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(id: Int, chapter: Int, chapters: Int) {
        val action = ListEventFragmentDirections
            .actionBottomRemindersToViewEventFragment(
                eventId = id,
                chapter = chapter,
                chapters = chapters
            )
        navController.navigate(action)
    }

    override fun onItemCheckedClick(id: Int, state: Boolean) {
        viewModel.updateEventState(id, state)
    }
}