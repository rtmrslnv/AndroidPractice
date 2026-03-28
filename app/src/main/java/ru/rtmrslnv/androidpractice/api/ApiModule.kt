package ru.rtmrslnv.androidpractice.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun providesBaseUrl() : String = "https://himalayas.app/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl : String) : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    @Provides
    @Singleton
    fun provideJobService(retrofit: Retrofit) : JobService = retrofit.create(JobService::class.java)

    @Provides
    @Singleton
    fun provideHimalayasService(jobService: JobService) : HimalayasService = HimalayasService(jobService)
}