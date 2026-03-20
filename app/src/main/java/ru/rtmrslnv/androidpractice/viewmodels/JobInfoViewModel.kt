package ru.rtmrslnv.androidpractice.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.rtmrslnv.androidpractice.models.JobInfoRepository
import ru.rtmrslnv.androidpractice.models.JobInfoUI
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml

class JobInfoViewModel(private val jobInfoRepository: JobInfoRepository) : ViewModel() {
    private val _jobInfos = mutableStateOf<List<JobInfoUI>>(emptyList())
    val jobInfos: State<List<JobInfoUI>> = _jobInfos;

    init {
        getJobInfos()
    }

    private fun getJobInfos() {
        viewModelScope.launch {
            _jobInfos.value = jobInfoRepository.getJobInfos().map {
                val hasSalary = it.minSalary != null
                val salary = if (hasSalary) "${it.minSalary} - ${it.maxSalary} ${it.currency}" else "N/A"
                JobInfoUI(
                    it.id, it.title,
                    it.companyName,
                    it.employmentType,
                    salary,
                    hasSalary,
                    it.currency,
                    AnnotatedString.fromHtml(it.description),
                    // TODO: load image
                    Icons.Default.AccountBox
                )
            }
        }
    }

    public fun findJobInfo(id: Int): JobInfoUI? {
        return jobInfos.value.find { it.id == id }
    }
}