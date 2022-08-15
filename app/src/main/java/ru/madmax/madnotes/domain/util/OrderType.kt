package ru.madmax.madnotes.domain.util

sealed interface OrderType{
    object Ascending: OrderType
    object Descending: OrderType
}