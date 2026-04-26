package ru.rtmrslnv.androidpractice.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.application
import retrofit2.Response
import ru.rtmrslnv.androidpractice.api.HimalayasService
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val application: Application) {
    fun load() : ProfileModel {
        val prefs = application.getSharedPreferences("profileData", Context.MODE_PRIVATE)
        val name = prefs.getString("name", "") ?: ""
        val avatarUri = prefs.getString("avatarUrl", "") ?: ""
        val portfolioUrl = prefs.getString("portfolioUrl", "") ?: ""
        val favoriteClassTime = LocalTime.parse(prefs.getString("favoriteClassTime", "00:00"),
            DateTimeFormatter.ofPattern("HH:mm"))
        return ProfileModel(name, avatarUri, portfolioUrl, favoriteClassTime)
    }

    fun save(profile : ProfileModel) {
        val prefs = application.getSharedPreferences("profileData", Context.MODE_PRIVATE)
        with (prefs.edit()) {
            putString("name", profile.name)
            putString("avatarUri", profile.avatarUri)
            putString("portfolioUrl", profile.portfolioUrl)
            putString("favoriteClassTime", profile.favoriteClassTime.format(DateTimeFormatter.ofPattern("HH:mm")))
            apply()
        }
    }
}