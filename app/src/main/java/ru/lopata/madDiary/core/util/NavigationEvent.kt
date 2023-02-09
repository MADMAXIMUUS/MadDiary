package ru.lopata.madDiary.core.util

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class NavigationEvent : Parcelable {
    data class Create(val name: String) : NavigationEvent()
    data class Update(val name: String) : NavigationEvent()
    data class Delete(val passObject: Bundle) : NavigationEvent()
}
