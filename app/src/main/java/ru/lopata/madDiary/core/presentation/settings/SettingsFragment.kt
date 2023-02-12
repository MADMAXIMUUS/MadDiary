package ru.lopata.madDiary.core.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            settingsThemeRbRoot.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.settings_rb_system -> {
                        viewModel.setSystemTheme()
                    }
                    R.id.settings_rb_light -> {
                        viewModel.setLightTheme()
                    }
                    R.id.settings_rb_dark -> {
                        viewModel.setDarkTheme()
                    }
                }
            }
            settingsNotificationEventRoot.setOnClickListener {
                val intent = Intent(ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(EXTRA_APP_PACKAGE, requireContext().packageName)
                    putExtra(EXTRA_CHANNEL_ID, "eventAlarm")
                }
                startActivity(intent)
            }
            settingsNotificationTaskRoot.setOnClickListener {
                val intent = Intent(ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(EXTRA_APP_PACKAGE, requireContext().packageName)
                    putExtra(EXTRA_CHANNEL_ID, "taskAlarm")
                }
                startActivity(intent)
            }
            settingsNotificationReminderRoot.setOnClickListener {
                val intent = Intent(ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(EXTRA_APP_PACKAGE, requireContext().packageName)
                    putExtra(EXTRA_CHANNEL_ID, "reminderAlarm")
                }
                startActivity(intent)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.settings.collectLatest { settings ->
                when (settings.theme) {
                    ThemeEnum.LIGHT -> {
                        binding.settingsThemeRbRoot.check(R.id.settings_rb_light)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    ThemeEnum.DARK -> {
                        binding.settingsThemeRbRoot.check(R.id.settings_rb_dark)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    ThemeEnum.SYSTEM -> {
                        binding.settingsThemeRbRoot.check(R.id.settings_rb_system)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}