package ru.rtmrslnv.androidpractice.models

data class JobInfo(
    val title: String,
    val excerpt: String,
    val companyName: String,
    val companySlug: String,
    val companyLogo: String?,
    val employmentType: String?,
    val minSalary: Double?,
    val maxSalary: Double?,
    val seniority: List<String> = emptyList(),
    val currency: String?,
    val locationRestrictions: List<String> = emptyList(),
    val timezoneRestrictions: List<Double> = emptyList(),
    val categories: List<String> = emptyList(),
    val parentCategories: List<String> = emptyList(),
    val description: String,
    val pubDate: Long,
    val expiryDate: Long,
    val applicationLink: String,
    val guid: String
)