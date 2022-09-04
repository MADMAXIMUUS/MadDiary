package ru.madmax.madnotes.core.util

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
    object Save : UiEvent()
}
