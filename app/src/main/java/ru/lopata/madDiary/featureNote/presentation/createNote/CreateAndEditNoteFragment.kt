package ru.lopata.madDiary.featureNote.presentation.createNote

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.UiEvent
import ru.lopata.madDiary.databinding.FragmentCreateAndEditNoteBinding

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
                        viewModel.saveNote()
                    }
                    R.id.create_menu_delete -> {

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
                    is UiEvent.Save -> {
                        view.findNavController().navigateUp()
                    }
                    is UiEvent.Delete -> {
                    }
                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentNote.collectLatest { note ->
                binding.apply {
                    createAndEditNoteTitleEdt.setText(note.title)
                    createAndEditNoteTitleEdt.setSelection(note.title.length)
                    createAndEditNoteDataEdt.setText(note.text)
                    createAndEditNoteDataEdt.setSelection(note.text.length)
                }
            }
        }


        binding.apply {
            createAndEditNoteTitleEdt.addTextChangedListener(afterTextChanged = { text ->
                viewModel.noteTitleChange(text.toString())
            })

            createAndEditNoteDataEdt.addTextChangedListener(afterTextChanged = { text ->
                viewModel.noteDescriptionChange(text.toString())
            })

            binding.apply {
                createAndEditNoteBackButton.setOnClickListener {
                    view.findNavController().navigateUp()
                }

                createAndEditNoteConfirmButton.setOnClickListener {
                    viewModel.saveNote()
                }

                createAndEditNoteDeleteButton.setOnClickListener {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}