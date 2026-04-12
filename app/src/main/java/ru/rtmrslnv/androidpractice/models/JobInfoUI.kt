package ru.rtmrslnv.androidpractice.models

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JobInfoUI(
    @PrimaryKey var id: Int,
    @ColumnInfo("title") var title: String,
    @ColumnInfo("companyName") var companyName: String,
    @ColumnInfo("employmentType") var employmentType: String,
    @ColumnInfo("salary") var salary: String,
    @ColumnInfo("hasSalary") var hasSalary: Boolean,
    @ColumnInfo("currency") var currency: String,
    @ColumnInfo("description") var description: AnnotatedString,
    @ColumnInfo("companyLogo") var companyLogo: ImageBitmap
)
