package ru.madmax.madnotes.presentation.create_note

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.madnotes.R
import ru.madmax.madnotes.databinding.FragmentCreateAndEditNoteBinding

@AndroidEntryPoint
class CreateAndEditNoteFragment : Fragment() {

    private var _binding: FragmentCreateAndEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAndEditNoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAndEditNoteBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.create_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.create_menu_save -> {

                    }
                    R.id.create_menu_delete -> {

                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}