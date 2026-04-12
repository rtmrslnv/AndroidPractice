package ru.rtmrslnv.androidpractice.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.application
import retrofit2.Response
import ru.rtmrslnv.androidpractice.api.HimalayasService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(private val application: Application) {
    fun load() : SettingsModel {
        val prefs = application.getSharedPreferences("searchSettings", Context.MODE_PRIVATE)
        val q = prefs.getString("q", "") ?: ""
        val sort = prefs.getString("sortMode", "") ?: SortMode.RELEVANT.value
        return SettingsModel(q, SortMode.fromValue(sort) ?: SortMode.RELEVANT)
    }

    fun save(settings : SettingsModel) {
        val prefs = application.getSharedPreferences("searchSettings", Context.MODE_PRIVATE)
        with (prefs.edit()) {
            putString("q", settings.q)
            putString("sortMode", settings.sortMode.value)
            apply()
        }
    }
}