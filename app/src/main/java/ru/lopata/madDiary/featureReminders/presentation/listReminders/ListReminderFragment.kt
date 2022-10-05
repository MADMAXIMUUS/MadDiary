package ru.lopata.madDiary.featureReminders.presentation.listReminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.madmax.madDiary.databinding.FragmentListReminderBinding
import ru.madmax.madDiary.featureReminders.presentation.bottomsheet.BottomSheetCreateReminderFragment

@AndroidEntryPoint
class ListReminderFragment : Fragment() {

    private var _binding: FragmentListReminderBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        binding.reminderListBtnCreateReminder.setOnClickListener {
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