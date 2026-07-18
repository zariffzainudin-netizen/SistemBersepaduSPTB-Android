package com.kuskop.sptb.core.di

import com.kuskop.sptb.core.network.SptbApiService
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
