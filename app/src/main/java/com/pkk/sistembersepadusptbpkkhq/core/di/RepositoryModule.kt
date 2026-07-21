package com.pkk.sistembersepadusptbpkkhq.core.di

import com.pkk.sistembersepadusptbpkkhq.core.domain.repository.SptbRepository
import com.pkk.sistembersepadusptbpkkhq.core.domain.repository.SptbRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSptbRepository(impl: SptbRepositoryImpl): SptbRepository
}
