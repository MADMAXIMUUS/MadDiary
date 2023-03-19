package ru.lopata.madDiary.featureNote.presentation.createNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                            view.findNavController().navigateUp()
                        }
                        is UiEvent.Delete -> {
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
                        createAndEditNoteTitleEdt.setText(note.title)
                        createAndEditNoteTitleEdt.setSelection(note.title.length)
                        createAndEditNoteDataEdt.setText(note.text)
                        createAndEditNoteDataEdt.setSelection(note.text.length)
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

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}