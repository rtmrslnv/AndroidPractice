package ru.rtmrslnv.androidpractice.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.rtmrslnv.androidpractice.models.JobInfoRepository
import ru.rtmrslnv.androidpractice.models.JobInfoUI
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.room.Room
import com.bumptech.glide.Glide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import ru.rtmrslnv.androidpractice.models.JobInfo
import ru.rtmrslnv.androidpractice.models.JobsApiResponse
import javax.inject.Inject
import kotlin.collections.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rtmrslnv.androidpractice.models.SettingsModel
import ru.rtmrslnv.androidpractice.models.SettingsRepository
import ru.rtmrslnv.androidpractice.models.SortMode

@HiltViewModel
class FavoritesViewModel @Inject constructor(application: Application, private val jobInfoRepository: JobInfoRepository) : AndroidViewModel(application) {
    val jobInfos = MutableLiveData<List<JobInfoUI>>(emptyList())
    val error = mutableStateOf(false)

    init {
        loadJobs()
    }

    fun loadJobs() {
        viewModelScope.launch {
            jobInfoRepository.getSaved().collect {
                    list -> jobInfos.value = list
            }
        }
    }

    private suspend fun loadBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        return@withContext try {
            Glide.with(getApplication<Application>().applicationContext)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            null
        }
    }

    public fun findJobInfo(id: Int): JobInfoUI? {
        return jobInfos.value?.find { it.id == id }
    }
}