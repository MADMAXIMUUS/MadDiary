package ru.madmax.madnotes.feature_note.presentation.list_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.madmax.madnotes.databinding.FragmentListCategoryBinding
import ru.madmax.madnotes.core.util.ListsItemDecoration

class ListCategoryFragment : Fragment() {


    private var _binding: FragmentListCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listCategoryAdapter = ListCategoryAdapter { note ->
            /*val action =
                ListCategoryFragmentDirections.actionBottomNotesToCreateNoteFragment(noteId = note.id!!)
            view.findNavController().navigate(action)*/
        }

        binding.categoryListRecycler.apply {
            adapter = listCategoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(ListsItemDecoration(20, 20))
            setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}