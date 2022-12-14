package ru.lopata.madDiary.featureNote.presentation.listNote

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.GridItemDecoration
import ru.lopata.madDiary.databinding.FragmentListNoteBinding

@AndroidEntryPoint
class ListNoteFragment : Fragment() {

    private var _binding: FragmentListNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListNoteViewModel by viewModels()
    private lateinit var navController: NavController

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

        val listNoteAdapter = ListNoteAdapter { note ->
            val action =
                ListNoteFragmentDirections.actionBottomNotesToCreateNoteFragment(noteId = note.id!!)
            view.findNavController().navigate(action)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.list_menu_search -> {
                        navController.navigate(R.id.action_bottom_notes_to_searchFragment)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.noteListRecycler.apply {
            adapter = listNoteAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridItemDecoration(30, 2))
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.listNoteState.collect { state ->
                    listNoteAdapter.submitList(state.notes)
                }
            }
        }

        binding.noteListBtnCreateNote.setOnClickListener {
            navController.navigate(R.id.action_bottom_notes_to_createNoteFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}