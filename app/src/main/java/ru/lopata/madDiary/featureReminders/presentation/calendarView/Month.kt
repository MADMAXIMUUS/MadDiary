package ru.lopata.madDiary.featureReminders.presentation.calendarView

import ru.lopata.madDiary.featureReminders.domain.model.CalendarModel

typealias OnCellChangeListener = (calendar: CalendarLayout) -> Unit

class CalendarLayout(
    val rows: Int,
    val columns: Int = 7
) {

    private val cells = Array(rows) { Array(columns) { CalendarModel() } }

    val listeners = mutableSetOf<OnCellChangeListener>()

    fun getCell(row: Int, column: Int): CalendarModel {
        if (row < 0 || column < 0 || row >= rows || column >= columns) return CalendarModel()
        return cells[row][column]
    }

    fun setCell(row: Int, column: Int, cell: CalendarModel) {
        if (row < 0 || column < 0 || row >= rows || column >= columns) return
        if (cells[row][column] != cell) {
            cells[row][column] = cell
            listeners.forEach { it.invoke(this) }
        }
    }

    override fun equals(other: Any?): Boolean {
        return cells.contentEquals((other as CalendarLayout).cells)
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + cells.contentDeepHashCode()
        result = 31 * result + listeners.hashCode()
        return result
    }
}