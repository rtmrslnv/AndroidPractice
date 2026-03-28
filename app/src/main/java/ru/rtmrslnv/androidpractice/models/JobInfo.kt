package ru.rtmrslnv.androidpractice.models

// https://himalayas.app/api
data class JobInfo(
    var id: Int,
    var title: String,
    var companyName: String,
    var employmentType: String,
    var minSalary: Int?,
    var maxSalary: Int?,
    var currency: String,
    var description: String,
    var companyLogoUrl: String?
)
