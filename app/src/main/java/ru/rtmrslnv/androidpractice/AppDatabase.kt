package ru.rtmrslnv.androidpractice

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.rtmrslnv.androidpractice.converters.AnnotatedStringConverter
import ru.rtmrslnv.androidpractice.converters.ImageBitmapConverter
import ru.rtmrslnv.androidpractice.models.JobInfoDao
import ru.rtmrslnv.androidpractice.models.JobInfoUI

@Database(entities = [JobInfoUI::class], version = 1)
@TypeConverters(AnnotatedStringConverter::class, ImageBitmapConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jobInfoDao(): JobInfoDao
}