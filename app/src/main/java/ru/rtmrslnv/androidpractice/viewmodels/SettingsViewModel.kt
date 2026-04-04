package ru.rtmrslnv.androidpractice.viewmodels;

import android.app.Application;
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import ru.rtmrslnv.androidpractice.models.SettingsModel
import ru.rtmrslnv.androidpractice.models.SettingsRepository
import ru.rtmrslnv.androidpractice.models.SortMode

@HiltViewModel
class SettingsViewModel @Inject constructor(application: Application, private val settingsRepository: SettingsRepository) : AndroidViewModel(application) {
    public val settings = mutableStateOf<SettingsModel>(SettingsModel("", SortMode.RELEVANT))

    init {
        loadSettings()
    }

    private fun loadSettings() {
       settings.value = settingsRepository.load()
    }

    fun saveSettings() {
        settingsRepository.save(settings.value)
    }

    fun isDefault() : Boolean {
        return settings.value.q.isEmpty() && settings.value.sortMode == SortMode.RELEVANT
    }
}
