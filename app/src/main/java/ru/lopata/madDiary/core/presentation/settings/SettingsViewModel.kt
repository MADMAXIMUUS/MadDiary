package ru.lopata.madDiary.core.presentation.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.lopata.madDiary.core.presentation.settings.ThemeEnum.Companion.toThemeEnum
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
        when (themeString.toThemeEnum()) {
            ThemeEnum.LIGHT -> {
                _settings.update { currentState ->
                    currentState.copy(
                        theme = ThemeEnum.LIGHT
                    )
                }
            }
            ThemeEnum.DARK -> {
                _settings.update { currentState ->
                    currentState.copy(
                        theme = ThemeEnum.DARK
                    )
                }
            }
            ThemeEnum.SYSTEM -> {
                _settings.update { currentState ->
                    currentState.copy(
                        theme = ThemeEnum.SYSTEM
                    )
                }
            }
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
}