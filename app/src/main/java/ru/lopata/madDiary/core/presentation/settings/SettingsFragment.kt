package ru.lopata.madDiary.core.presentation.settings

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.showToast
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

        var checkedCardView: MaterialCardView = binding.iconDefault

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

            iconDefault.setOnClickListener {
                val manager: PackageManager = requireActivity().packageManager

                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.Halloween"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.NewYear"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.core.presentation.MainActivity"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                requireActivity().showToast("Default icon")
                viewModel.updateIcon(IconEnum.DEFAULT)
            }

            iconNewYear.setOnClickListener {
                val manager: PackageManager = requireActivity().packageManager
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.Halloween"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.core.presentation.MainActivity"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.NewYear"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                requireActivity().showToast("New Year icon")

                viewModel.updateIcon(IconEnum.NEW_YEAR)
            }

            iconHalloween.setOnClickListener {

                val manager: PackageManager = requireActivity().packageManager
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.NewYear"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.core.presentation.MainActivity"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                manager.setComponentEnabledSetting(
                    ComponentName(
                        requireActivity(),
                        "ru.lopata.madDiary.Halloween"
                    ),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                requireActivity().showToast("Halloween icon")

                viewModel.updateIcon(IconEnum.HALLOWEEN)
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
                when (settings.icon) {
                    IconEnum.DEFAULT -> {
                        checkedCardView.strokeWidth = 0
                        binding.iconDefault.strokeWidth = 5
                        checkedCardView = binding.iconDefault
                    }
                    IconEnum.NEW_YEAR -> {
                        checkedCardView.strokeWidth = 0
                        binding.iconNewYear.strokeWidth = 5
                        checkedCardView = binding.iconNewYear
                    }
                    IconEnum.HALLOWEEN -> {
                        checkedCardView.strokeWidth = 0
                        binding.iconHalloween.strokeWidth = 5
                        checkedCardView = binding.iconHalloween
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