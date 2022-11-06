package ru.lopata.madDiary.core.util

import android.os.Bundle
import androidx.annotation.StringRes

sealed class UiEvent {
    data class ShowSnackBar(@StringRes val message: Int) : UiEvent()
    object Save : UiEvent()
    data class Edit(val passObject: Bundle) : UiEvent()
    object Delete : UiEvent()
}
