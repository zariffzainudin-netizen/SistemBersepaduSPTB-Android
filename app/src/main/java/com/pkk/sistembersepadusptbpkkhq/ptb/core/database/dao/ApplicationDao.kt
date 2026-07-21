package com.pkk.sistembersepadusptbpkkhq.core.database.dao

import androidx.room.*
import com.pkk.sistembersepadusptbpkkhq.core.database.entity.ApplicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications ORDER BY row_index DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE row_index = :rowIndex")
    suspend fun getByRowIndex(rowIndex: Int): ApplicationEntity?

    @Query("SELECT * FROM applications WHERE syarikat LIKE '%' || :query || '%' OR cidb LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE jenis = :jenis")
    fun filterByType(jenis: String): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE syor_status = :status")
    fun filterByStatus(status: String): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(applications: List<ApplicationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: ApplicationEntity)

    @Query("DELETE FROM applications")
    suspend fun deleteAll()

    @Query("DELETE FROM applications WHERE row_index = :rowIndex")
    suspend fun deleteByRowIndex(rowIndex: Int)

    @Query("SELECT COUNT(*) FROM applications")
    suspend fun count(): Int
}
