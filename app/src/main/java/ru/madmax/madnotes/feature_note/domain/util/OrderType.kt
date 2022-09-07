package ru.madmax.madnotes.feature_note.domain.util

sealed interface OrderType{
    object Ascending: OrderType
    object Descending: OrderType
}