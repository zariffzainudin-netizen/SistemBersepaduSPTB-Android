package com.kuskop.sptb.core.database.dao

import androidx.room.*
import com.kuskop.sptb.core.database.entity.InboxEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InboxDao {
    @Query("SELECT * FROM inbox ORDER BY masa DESC")
    fun getAll(): Flow<List<InboxEntity>>

    @Query("SELECT * FROM inbox WHERE mesej LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<InboxEntity>>

    @Query("UPDATE inbox SET dibaca = 1 WHERE id = :id")
    suspend fun markRead(id: String)

    @Query("UPDATE inbox SET dibaca = 1 WHERE dibaca = 0")
    suspend fun markAllRead()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<InboxEntity>)

    @Delete
    suspend fun delete(message: InboxEntity)

    @Query("DELETE FROM inbox WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("DELETE FROM inbox")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM inbox WHERE dibaca = 0")
    fun unreadCount(): Flow<Int>
}
