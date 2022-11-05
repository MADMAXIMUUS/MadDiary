package ru.lopata.madDiary.core.util

import androidx.annotation.StringRes

sealed class UiEvent {
    data class ShowSnackBar(@StringRes val message: Int) : UiEvent()
    object Save : UiEvent()
    object Edit : UiEvent()
    object Delete : UiEvent()
}
