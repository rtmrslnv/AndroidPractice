package ru.rtmrslnv.androidpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.rtmrslnv.androidpractice.models.JobInfoRepository
import ru.rtmrslnv.androidpractice.viewmodels.FavoritesViewModel
import ru.rtmrslnv.androidpractice.viewmodels.JobInfoViewModel
import ru.rtmrslnv.androidpractice.viewmodels.SettingsViewModel
import ru.rtmrslnv.androidpractice.views.MainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val jobInfoViewModel by viewModels<JobInfoViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val favoritesViewModel by viewModels<FavoritesViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(jobInfoViewModel, settingsViewModel, favoritesViewModel)
        }
    }
}