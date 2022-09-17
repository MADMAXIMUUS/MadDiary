package ru.madmax.madDiary.featureNote.domain.util

sealed interface OrderType{
    object Ascending: OrderType
    object Descending: OrderType
}