package com.kuskop.sptb.core.di

import android.content.Context
import com.kuskop.sptb.core.database.SptbDatabase
import com.kuskop.sptb.core.database.dao.ApplicationDao
import com.kuskop.sptb.core.database.dao.InboxDao
import com.kuskop.sptb.core.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SptbDatabase {
        return SptbDatabase.create()
    }

    @Provides
    fun provideApplicationDao(db: SptbDatabase): ApplicationDao = db.applicationDao()

    @Provides
    fun provideUserDao(db: SptbDatabase): UserDao = db.userDao()

    @Provides
    fun provideInboxDao(db: SptbDatabase): InboxDao = db.inboxDao()
}
