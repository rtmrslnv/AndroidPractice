package ru.rtmrslnv.androidpractice.models

import java.time.LocalTime

data class ProfileModel(
    val name : String,
    var avatarUri: String,
    var portfolioUrl: String,
    var favoriteClassTime: LocalTime
)
