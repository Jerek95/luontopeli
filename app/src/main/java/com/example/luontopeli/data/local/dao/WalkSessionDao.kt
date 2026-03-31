package com.example.luontopeli.data.local.dao


import androidx.room.*
import com.example.luontopeli.data.local.entity.WalkSession
import kotlinx.coroutines.flow.Flow


@Dao
interface WalkSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spot: WalkSession): Long

    @Query("SELECT * FROM nature_spots ORDER BY timestamp DESC")
    fun getAllSpots(): Flow<List<WalkSession>>

    @Query("SELECT * FROM nature_spots WHERE latitude != 0.0")
    fun getSpotsWithLocation(): Flow<List<WalkSession>>

    @Query("SELECT * FROM nature_spots WHERE synced = 0")
    suspend fun getUnsyncedSpots(): List<WalkSession>

    @Query("UPDATE nature_spots SET synced = 1, imageFirebaseUrl = :url WHERE id = :id")
    suspend fun markSynced(id: String, url: String)

    @Delete
    suspend fun delete(spot: WalkSession)
}