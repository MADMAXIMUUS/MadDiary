package ru.lopata.madDiary.featureReminders.presentation.listReminders

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
import ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomsheet.BottomSheetCreateReminderFragment

@AndroidEntryPoint
class ListEventFragment : Fragment() {

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

        val eventAdapter = ListEventAdapter()

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
            BottomSheetCreateReminderFragment(navController).show(
                requireActivity().supportFragmentManager,
                "ChooseReminderType"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}