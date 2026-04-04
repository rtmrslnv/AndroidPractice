package ru.rtmrslnv.androidpractice.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.rtmrslnv.androidpractice.models.JobsApiResponse

interface JobService {
    @GET("jobs/api")
    suspend fun getJobs(
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : Response<JobsApiResponse>

    @GET("jobs/api/search")
    suspend fun search(
        @Query("q") q : String,
        @Query("sort") sort : String,
        @Query("page") page : Int
    ) : Response<JobsApiResponse>
}