package ru.lopata.madDiary.core.util

data class EditTextState(
    val text: String = "",
    val cursorPosition: Int = 0,
    val isEmpty: Boolean = false,
    val isError: Boolean = false
)
