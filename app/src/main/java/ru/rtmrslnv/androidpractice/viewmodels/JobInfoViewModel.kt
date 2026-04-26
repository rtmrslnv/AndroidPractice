package ru.rtmrslnv.androidpractice.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.rtmrslnv.androidpractice.models.JobInfoRepository
import ru.rtmrslnv.androidpractice.models.JobInfoUI
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.withContext
import ru.rtmrslnv.androidpractice.models.JobInfo
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import ru.rtmrslnv.androidpractice.models.SettingsModel
import ru.rtmrslnv.androidpractice.models.SettingsRepository

@HiltViewModel
class JobInfoViewModel @Inject constructor(application: Application, private val jobInfoRepository: JobInfoRepository, private val settingsRepository: SettingsRepository) : AndroidViewModel(application) {
    val jobInfos = MutableStateFlow<List<JobInfoUI>>(emptyList())
    val error = mutableStateOf(false)
    private var page = 1
    private var isLoading = false
    private var lastSettings = settingsRepository.load()


    init {
        loadJobs()
    }

    fun loadJobs() {
        val currentSettings = settingsRepository.load()
        if (currentSettings != lastSettings) {
            page = 1
            jobInfos.value = emptyList()
        }
        loadJobs(currentSettings, page)
    }

    fun mustUpdate() : Boolean {
        return settingsRepository.load() != lastSettings
    }

    fun save(jobInfo: JobInfoUI) {
        viewModelScope.launch {
            jobInfoRepository.save(jobInfo)
        }
    }

    private suspend fun mapJobInfosToUI(jobs: List<JobInfo>, startIndex: Int): List<JobInfoUI> {
        return jobs.mapIndexed { idx, it ->
            val hasSalary = it.minSalary != null
            val salary = if (hasSalary) "${it.minSalary} - ${it.maxSalary} ${it.currency}" else "N/A"
            var logo = ImageBitmap(1, 1)
            if (!it.companyLogo.isNullOrEmpty()) {
                logo = loadBitmap(it.companyLogo)?.asImageBitmap() ?: ImageBitmap(1, 1)
            }
            JobInfoUI(
                startIndex + idx, it.title,
                it.companyName,
                it.employmentType ?: "N/A",
                salary,
                hasSalary,
                it.currency ?: "N/A",
                AnnotatedString.fromHtml(it.description),
                logo
            )
        }
    }

    private fun loadJobs(searchSettings: SettingsModel, page: Int) {
        if (isLoading) {
            return
        }

        isLoading = true

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            error.value = true
            isLoading = false
        }

        viewModelScope.launch(exceptionHandler) {
            val resp = jobInfoRepository.search(searchSettings.q, searchSettings.sortMode.value, page)

            if (resp.isSuccessful) {
                val body = resp.body()
                if (!body?.jobs.isNullOrEmpty()) {
                    val uiList = mapJobInfosToUI(body.jobs, jobInfos.value.size)
                    jobInfos.value = jobInfos.value + uiList
                    this@JobInfoViewModel.page += 1
                    error.value = false
                    lastSettings = searchSettings
                    isLoading = false
                }
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