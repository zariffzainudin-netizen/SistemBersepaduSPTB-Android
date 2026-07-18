package com.kuskop.sptb.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kuskop.sptb.core.database.dao.ApplicationDao
import com.kuskop.sptb.core.database.dao.InboxDao
import com.kuskop.sptb.core.database.dao.UserDao
import com.kuskop.sptb.core.database.entity.ApplicationEntity
import com.kuskop.sptb.core.database.entity.CacheMetaEntity
import com.kuskop.sptb.core.database.entity.InboxEntity
import com.kuskop.sptb.core.database.entity.UserEntity

@Database(
    entities = [
        ApplicationEntity::class,
        UserEntity::class,
        InboxEntity::class,
        CacheMetaEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class SptbDatabase : RoomDatabase() {
    abstract fun applicationDao(): ApplicationDao
    abstract fun userDao(): UserDao
    abstract fun inboxDao(): InboxDao

    companion object {
        const val DB_NAME = "sptb_database"

        fun create(): SptbDatabase {
            return Room.databaseBuilder(
                SptbApplicationHolder.context,
                SptbDatabase::class.java,
                DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}

object SptbApplicationHolder {
    lateinit var context: android.content.Context
}
