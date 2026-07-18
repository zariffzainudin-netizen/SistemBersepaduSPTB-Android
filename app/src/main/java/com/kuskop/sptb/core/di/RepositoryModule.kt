package com.kuskop.sptb.core.di

import com.kuskop.sptb.core.domain.repository.SptbRepository
import com.kuskop.sptb.core.domain.repository.SptbRepositoryImpl
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
