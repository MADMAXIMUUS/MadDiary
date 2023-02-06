package ru.lopata.madDiary.core.util

import android.os.Bundle
import androidx.annotation.StringRes

sealed class UiEvent {
    data class ShowSnackBar(@StringRes val message: Int) : UiEvent()
    data class Save(val id: Long) : UiEvent()
    data class Edit(val passObject: Bundle) : UiEvent()
    object UpdateUiState : UiEvent()
    object Delete : UiEvent()
}
