package ru.lopata.madDiary.featureNote.presentation.listNote

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.GridItemDecoration
import ru.lopata.madDiary.core.util.NavigationEvent
import ru.lopata.madDiary.databinding.FragmentListNoteBinding
import ru.lopata.madDiary.featureNote.domain.model.NoteItem
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureReminders.presentation.listEvents.adapters.CompositeAdapter

@AndroidEntryPoint
class ListNoteFragment : Fragment(), ListNoteAdapter.OnItemClick {

    private var _binding: FragmentListNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListNoteViewModel by viewModels()
    private lateinit var navController: NavController

    private val compositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(ListNoteAdapter(this))
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListNoteBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        binding.noteListSearchButton.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        binding.noteListRecycler.apply {
            adapter = compositeAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecoration(30, 30, 2))
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.listNoteState.collect { state ->
                    compositeAdapter.submitList(state.notes)
                }
            }
        }

        binding.noteListBtnCreateNote.setOnClickListener {
            navController.navigate(R.id.action_bottom_notes_to_createNoteFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        when (val event = ListNoteFragmentArgs.fromBundle(requireArguments()).navigationEvents) {
            is NavigationEvent.Delete -> {
                val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    event.passObject.getParcelable(
                        "note",
                        Note::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    event.passObject.getParcelable("note")
                }
                if (item != null) {
                    Snackbar
                        .make(
                            binding.root, getString(
                                R.string.event_deleted,
                                item.title
                            ), Snackbar.LENGTH_SHORT
                        )
                        .setAnchorView(binding.noteListBtnCreateNote)
                        .setAction(R.string.undo) {
                            viewModel.undoDelete(item)
                        }
                        .show()
                }
            }
            is NavigationEvent.Create -> {
                Snackbar
                    .make(
                        binding.root,
                        getString(
                            R.string.event_created,
                            event.name
                        ),
                        Snackbar.LENGTH_SHORT
                    )
                    .setAnchorView(binding.noteListBtnCreateNote)
                    .show()
            }
            else -> {}
        }

        requireArguments().clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(note: NoteItem) {
        val action =
            ListNoteFragmentDirections.actionBottomNotesToCreateNoteFragment(noteId = note.noteId)
        navController.navigate(action)
    }
}