package com.example.tamo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TabDao {
    @Query("SELECT * FROM tabs ORDER BY id ASC")
    fun getAllTabs(): Flow<List<Tab>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tab: Tab): Long

    @Update
    suspend fun update(tab: Tab)

    @Delete
    suspend fun delete(tab: Tab)
}