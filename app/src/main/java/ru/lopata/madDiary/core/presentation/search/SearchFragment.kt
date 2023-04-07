package ru.lopata.madDiary.core.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.VerticalItemDecoration
import ru.lopata.madDiary.databinding.FragmentSearchBinding
import ru.lopata.madDiary.featureNote.domain.model.NoteItem
import ru.lopata.madDiary.featureNote.presentation.listNote.ListNoteAdapter
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.presentation.listEvents.adapters.CompositeAdapter
import ru.lopata.madDiary.featureReminders.presentation.listEvents.adapters.EventsAdapter


@AndroidEntryPoint
class SearchFragment : Fragment(), EventsAdapter.OnItemClickListener, ListNoteAdapter.OnItemClick {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchViewModel>()

    private var navController: NavController? = null

    private val compositeAdapter by lazy {
        CompositeAdapter.Builder().add(ListNoteAdapter(this)).add(EventsAdapter(this)).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        binding.apply {
            searchBackButton.setOnClickListener {
                view.findNavController().navigateUp()
            }
            searchField.addTextChangedListener(afterTextChanged = {
                viewModel.updateSearchQuery(it.toString())
            }, onTextChanged = { text, _, _, _ ->
                if (text?.isNotEmpty() == true) {
                    searchField.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                } else {
                    searchField.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.search_icon_wrap, 0
                    )
                }
            })
            searchField.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        viewModel.search()
                        true
                    }
                    else -> false
                }
            }

            searchRv.apply {
                adapter = compositeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(VerticalItemDecoration(30, 30))
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest { state ->
                if (binding.searchField.text.isEmpty()) {
                    binding.searchField.setText(state.searchQuery)
                }
                /*if (state.searchQuery.isNotEmpty() && state.searchResults.isEmpty()) {
                    Snackbar.make(requireView(), R.string.empty_search, Snackbar.LENGTH_SHORT)
                        .show()
                }*/

                compositeAdapter.submitList(state.searchResults)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(id: Int, chapter: Int, chapters: Int, type: Event.Types) {
        val action = when (type) {
            Event.Types.EVENT -> {
                SearchFragmentDirections.actionGlobalViewEventFragment(id)
            }
            Event.Types.TASK -> {
                SearchFragmentDirections.actionGlobalViewTaskFragment(id)
            }
            Event.Types.REMINDER -> {
                SearchFragmentDirections.actionGlobalViewReminderFragment(id)
            }
        }
        navController?.navigate(action)
    }

    override fun onItemCheckedClick(id: Int, state: Boolean) {
        viewModel.updateEventState(id, state)
    }

    override fun onItemClick(note: NoteItem) {
        val action = SearchFragmentDirections.actionSearchFragmentToCreateNoteFragment(note.noteId)
        navController?.navigate(action)
    }

}