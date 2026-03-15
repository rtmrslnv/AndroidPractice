package ru.rtmrslnv.androidpractice.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.rtmrslnv.androidpractice.models.JobInfo
import ru.rtmrslnv.androidpractice.models.JobInfoRepository

class JobInfoViewModel(private val jobInfoRepository: JobInfoRepository) : ViewModel() {
    private val _jobInfos = mutableStateOf<List<JobInfo>>(emptyList())
    val jobInfos: State<List<JobInfo>> = _jobInfos;

    init {
        getJobInfos()
    }

    private fun getJobInfos() {
        viewModelScope.launch {
            _jobInfos.value = jobInfoRepository.getJobInfos()
        }
    }
}