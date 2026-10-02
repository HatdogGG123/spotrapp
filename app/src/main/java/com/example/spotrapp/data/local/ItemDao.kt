package com.example.spotrapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// dao
@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: ItemEntity): Long

    @Update
    suspend fun update(item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    @Query("SELECT * FROM items ORDER BY id DESC")
    fun getAllItems(): Flow<List<ItemEntity>>

    // used by the duplicate name check in ItemViewModel
    @Query("SELECT * FROM items WHERE name = :name AND zone = :zone LIMIT 1")
    suspend fun findByNameAndZone(name: String, zone: String): ItemEntity?
}