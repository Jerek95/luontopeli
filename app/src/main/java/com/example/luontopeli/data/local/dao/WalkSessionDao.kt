package com.example.luontopeli.data.local.dao


import androidx.room.*
import com.example.luontopeli.data.local.entity.WalkSession
import kotlinx.coroutines.flow.Flow


@Dao
interface WalkSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spot: WalkSession): Long

    @Query("SELECT * FROM walk_sessions ORDER BY spotsFound DESC")
    fun getAllWalks(): Flow<List<WalkSession>>

    @Delete
    suspend fun delete(spot: WalkSession)
}