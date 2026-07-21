package com.pkk.sistembersepadusptbpkkhq.core.di

import com.pkk.sistembersepadusptbpkkhq.core.network.SptbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): SptbApiService = SptbApiService.create()
}
