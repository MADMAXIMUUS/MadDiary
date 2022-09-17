package ru.madmax.madDiary.core.util

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
    object Save : UiEvent()
}
