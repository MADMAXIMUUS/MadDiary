package ru.lopata.madDiary.core.presentation.settings

data class SettingScreenState(
    val theme: ThemeEnum = ThemeEnum.SYSTEM
)

enum class ThemeEnum {
    LIGHT, DARK, SYSTEM;

    companion object {
        fun String.toThemeEnum(): ThemeEnum {
            return try {
                valueOf(this)
            } catch (e: Exception) {
                SYSTEM
            }
        }
    }
}