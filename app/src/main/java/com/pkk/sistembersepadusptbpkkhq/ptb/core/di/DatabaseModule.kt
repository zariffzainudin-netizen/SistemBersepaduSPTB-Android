package com.pkk.sistembersepadusptbpkkhq.core.di

import android.content.Context
import com.pkk.sistembersepadusptbpkkhq.core.database.SptbDatabase
import com.pkk.sistembersepadusptbpkkhq.core.database.dao.ApplicationDao
import com.pkk.sistembersepadusptbpkkhq.core.database.dao.InboxDao
import com.pkk.sistembersepadusptbpkkhq.core.database.dao.UserDao
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
        return SptbDatabase.create(context)
    }

    @Provides
    fun provideApplicationDao(db: SptbDatabase): ApplicationDao = db.applicationDao()

    @Provides
    fun provideUserDao(db: SptbDatabase): UserDao = db.userDao()

    @Provides
    fun provideInboxDao(db: SptbDatabase): InboxDao = db.inboxDao()
}
