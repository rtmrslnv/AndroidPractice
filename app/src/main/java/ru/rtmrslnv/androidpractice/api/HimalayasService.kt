package ru.rtmrslnv.androidpractice.api

import retrofit2.Response
import ru.rtmrslnv.androidpractice.models.JobsApiResponse
import javax.inject.Inject

// https://himalayas.app/api
class HimalayasService @Inject constructor(private val jobService : JobService) {
    suspend fun getJobs(limit : Int, offset : Int) : Response<JobsApiResponse> = jobService.getJobs(limit, offset);
}