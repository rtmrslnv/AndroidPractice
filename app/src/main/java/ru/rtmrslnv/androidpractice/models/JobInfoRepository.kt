package ru.rtmrslnv.androidpractice.models

import retrofit2.Response
import ru.rtmrslnv.androidpractice.api.HimalayasService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobInfoRepository @Inject constructor(private val himalayasService: HimalayasService) {
    suspend fun getJobs(limit : Int, offset : Int) : Response<JobsApiResponse> = himalayasService.getJobs(limit, offset)
}