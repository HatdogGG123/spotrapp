package com.example.spotrapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ZoneDao {
    @Insert
    suspend fun insert(zone: ZoneEntity)

    @Delete
    suspend fun delete(zone: ZoneEntity)

    @Query("SELECT * FROM zones ORDER BY id ASC")
    fun getAllZones(): Flow<List<ZoneEntity>>

    @Query("SELECT * FROM zones WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): ZoneEntity?
}