package ru.madmax.madnotes.presentation.list_note

import android.os.Bundle
import android.view.*
import androidx.annotation.IdRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.madnotes.R
import ru.madmax.madnotes.databinding.FragmentListNoteBinding

@AndroidEntryPoint
class ListNoteFragment : Fragment(R.layout.fragment_list_note) {

    private var _binding: FragmentListNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListNoteViewModel by viewModels()

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

        val listNoteAdapter = ListNoteAdapter()

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.list_menu_search -> {
                        navigate(R.id.action_bottom_notes_to_searchFragment)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.noteListRecycler.apply {
            adapter = listNoteAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            listNoteAdapter.submitList(it)
        }

        binding.noteListBtnCreateNote.setOnClickListener {
            navigate(R.id.action_bottom_notes_to_createNoteFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun navigate(@IdRes route: Int) {
        binding.noteListRecycler.findNavController()
            .navigate(route)
    }
}