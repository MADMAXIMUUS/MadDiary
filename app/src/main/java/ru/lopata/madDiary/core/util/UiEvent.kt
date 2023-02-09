package ru.lopata.madDiary.core.util

import android.os.Bundle

sealed class UiEvent {
    data class Save(val id: Long) : UiEvent()
    data class Edit(val passObject: Bundle) : UiEvent()
    data class Update(val id: Long) : UiEvent()
    object Delete : UiEvent()
}
