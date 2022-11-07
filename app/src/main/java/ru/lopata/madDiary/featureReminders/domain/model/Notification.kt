package ru.lopata.madDiary.featureReminders.domain.model

data class Notification(
    val id: Int? = null,
    val eventOwnerId: Int = -1,
    val time: Long = 0
) {
    companion object {
        const val NEVER: Long = -1
        const val AT_TIME: Long = 0
        const val MINUTE_10: Long = 600000
        const val MINUTE_30: Long = MINUTE_10 * 3
        const val HOUR: Long = MINUTE_30 * 2
        const val DAY: Long = 86400000
        const val WEEK: Long = DAY * 7
        const val MONTH: Long = DAY * 30
    }
}