package ru.rtmrslnv.androidpractice.models

data class JobsApiResponse(
    val comments: String,
    val updatedAt: Int,
    val offset: Int,
    val limit: Int,
    val totalCount: Int,
    val jobs: List<JobInfo>
)
