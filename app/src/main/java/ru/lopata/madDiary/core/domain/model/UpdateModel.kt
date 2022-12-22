package ru.lopata.madDiary.core.domain.model

data class UpdateModel(
    val versionNumber: Int = 0,
    val versionTitle: String = "",
    val apkUrl: String = "empty",
    val updateDescription: String = ""
)
