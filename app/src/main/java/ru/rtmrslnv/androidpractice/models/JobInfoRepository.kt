package ru.rtmrslnv.androidpractice.models

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import ru.rtmrslnv.androidpractice.AppDatabase
import ru.rtmrslnv.androidpractice.api.HimalayasService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobInfoRepository @Inject constructor(private val himalayasService: HimalayasService, val dao : JobInfoDao) {
    suspend fun getJobs(limit : Int, offset : Int) : Response<JobsApiResponse> = himalayasService.getJobs(limit, offset)
    suspend fun search(q : String, sort : String, page : Int) : Response<JobsApiResponse> = himalayasService.search(q, sort, page)
    fun getSaved() : Flow<List<JobInfoUI>> {
        return dao.getAll()
    }

    suspend fun save(jobInfoUI : JobInfoUI) {
        dao.insert(jobInfoUI)
    }
}