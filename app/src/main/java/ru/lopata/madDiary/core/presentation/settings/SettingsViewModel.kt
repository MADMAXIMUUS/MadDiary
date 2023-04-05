package ru.lopata.madDiary.core.presentation.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.lopata.madDiary.core.presentation.settings.IconEnum.Companion.toIconEnum
import ru.lopata.madDiary.core.presentation.settings.ThemeEnum.Companion.toThemeEnum
import ru.lopata.madDiary.core.util.ICON
import ru.lopata.madDiary.core.util.THEME
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _settings = MutableStateFlow(SettingScreenState())
    val settings = _settings.asStateFlow()

    init {
        val themeString = sharedPreferences.getString(THEME, ThemeEnum.SYSTEM.toString()).toString()
        val iconString = sharedPreferences.getString(ICON, IconEnum.DEFAULT.toString()).toString()
        _settings.update { currentState ->
            currentState.copy(
                theme = themeString.toThemeEnum(),
                icon = iconString.toIconEnum()
            )
        }
    }

    fun setLightTheme() {
        _settings.update { currentState ->
            currentState.copy(
                theme = ThemeEnum.LIGHT
            )
        }
        sharedPreferences.edit().putString(THEME, ThemeEnum.LIGHT.toString()).apply()
    }

    fun setDarkTheme() {
        _settings.update { currentState ->
            currentState.copy(
                theme = ThemeEnum.DARK
            )
        }
        sharedPreferences.edit().putString(THEME, ThemeEnum.DARK.toString()).apply()
    }

    fun setSystemTheme() {
        _settings.update { currentState ->
            currentState.copy(
                theme = ThemeEnum.SYSTEM
            )
        }
        sharedPreferences.edit().putString(THEME, ThemeEnum.SYSTEM.toString()).apply()
    }

    fun updateIcon(icon: IconEnum) {
        _settings.update { currentState ->
            currentState.copy(
                icon = icon
            )
        }
        sharedPreferences.edit().putString(ICON, icon.toString()).apply()
    }
}