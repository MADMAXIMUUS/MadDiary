package ru.lopata.madDiary.featureNote.domain.util

sealed interface OrderType{
    object Ascending: OrderType
    object Descending: OrderType
}