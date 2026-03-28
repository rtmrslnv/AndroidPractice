package ru.rtmrslnv.androidpractice.models

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString

data class JobInfoUI(
    var id: Int,
    var title: String,
    var companyName: String,
    var employmentType: String,
    var salary: String,
    var hasSalary: Boolean,
    var currency: String,
    var description: AnnotatedString,
    var companyLogo: ImageBitmap
)
