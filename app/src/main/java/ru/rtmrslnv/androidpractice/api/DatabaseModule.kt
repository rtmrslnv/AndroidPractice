package ru.rtmrslnv.androidpractice.api

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.rtmrslnv.androidpractice.AppDatabase
import ru.rtmrslnv.androidpractice.converters.AnnotatedStringConverter
import ru.rtmrslnv.androidpractice.converters.ImageBitmapConverter
import ru.rtmrslnv.androidpractice.models.JobInfo
import ru.rtmrslnv.androidpractice.models.JobInfoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext ctx: Context, annotatedStringConverter: AnnotatedStringConverter, imageBitmapConverter: ImageBitmapConverter): AppDatabase {
        return Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
            .addTypeConverter(annotatedStringConverter)
            .addTypeConverter(imageBitmapConverter)
            .build()
    }

    @Provides
    fun providesJobInfoDao(db: AppDatabase) : JobInfoDao = db.jobInfoDao()
}