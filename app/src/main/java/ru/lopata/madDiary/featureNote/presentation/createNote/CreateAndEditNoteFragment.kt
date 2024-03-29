package ru.lopata.madDiary.featureNote.presentation.createNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.lopata.madDiary.core.util.NavigationEvent
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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is UiEvent.Save -> {
                            val action =
                                CreateAndEditNoteFragmentDirections.actionCreateNoteFragmentToBottomNotes(
                                    navigationEvents = NavigationEvent.Create(viewModel.currentNote.value.title)
                                )
                            view.findNavController().navigate(action)
                        }
                        is UiEvent.Delete -> {
                            val action =
                                CreateAndEditNoteFragmentDirections.actionCreateNoteFragmentToBottomNotes(
                                    navigationEvents = NavigationEvent.Delete(
                                        passObject = bundleOf(
                                            "note" to viewModel.currentNote.value
                                        )
                                    )
                                )
                            view.findNavController().navigate(action)
                        }
                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.currentNote.collectLatest { note ->
                    binding.apply {
                        if (createAndEditNoteDataEdt.text.isEmpty()) {
                            createAndEditNoteDataEdt.setText(note.text)
                        }
                        if (createAndEditNoteTitleEdt.text.isEmpty()) {
                            createAndEditNoteTitleEdt.setText(note.title)
                        }
                    }
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
                    viewModel.deleteNote()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}