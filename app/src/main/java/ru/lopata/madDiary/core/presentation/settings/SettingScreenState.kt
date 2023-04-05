package ru.lopata.madDiary.core.presentation.settings

data class SettingScreenState(
    val theme: ThemeEnum = ThemeEnum.SYSTEM,
    val icon: IconEnum = IconEnum.DEFAULT
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

enum class IconEnum {
    DEFAULT, NEW_YEAR, HALLOWEEN;

    companion object {
        fun String.toIconEnum(): IconEnum {
            return try {
                valueOf(this)
            } catch (e: Exception) {
                DEFAULT
            }
        }
    }
}