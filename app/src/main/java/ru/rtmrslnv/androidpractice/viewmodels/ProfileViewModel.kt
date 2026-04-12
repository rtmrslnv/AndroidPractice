package ru.rtmrslnv.androidpractice.viewmodels;

import android.app.Application;
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import ru.rtmrslnv.androidpractice.models.ProfileModel
import ru.rtmrslnv.androidpractice.models.ProfileRepository

@HiltViewModel
class ProfileViewModel @Inject constructor(application: Application, private val profileRepository: ProfileRepository) : AndroidViewModel(application) {
    public val profile = mutableStateOf<ProfileModel>(ProfileModel("", "", ""))

    init {
        loadProfile()
    }

    private fun loadProfile() {
        profile.value = profileRepository.load()
    }

    fun saveProfile() {
        profileRepository.save(profile.value)
    }
}
