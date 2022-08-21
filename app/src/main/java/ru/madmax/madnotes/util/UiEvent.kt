package ru.madmax.madnotes.util

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object Save : UiEvent()
}
