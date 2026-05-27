package ru.rtmrslnv.androidpractice.viewmodels;

import android.app.Application;
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.rtmrslnv.androidpractice.models.SettingsModel
import ru.rtmrslnv.androidpractice.models.SettingsRepository
import ru.rtmrslnv.androidpractice.models.SortMode

@HiltViewModel
class SettingsViewModel @Inject constructor(application: Application, private val settingsRepository: SettingsRepository) : AndroidViewModel(application) {
    private val _settings = MutableStateFlow<SettingsModel>(SettingsModel("", SortMode.RELEVANT))
    public val settings: StateFlow<SettingsModel> get() = _settings

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _settings.value = settingsRepository.load()
    }

    fun saveSettings() {
        settingsRepository.save(settings.value)
    }

    fun isDefault() : Boolean {
        return settings.value.q.isEmpty() && settings.value.sortMode == SortMode.RELEVANT
    }

    fun updateQuery(q: String) {
        _settings.update { it.copy(q = q) }
    }

    fun updateSortMode(sortMode: SortMode) {
        _settings.update { it.copy(sortMode = sortMode) }
    }

    fun reset() {
        _settings.value = SettingsModel("", SortMode.RELEVANT)
    }
}
