package com.example.tamo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks WHERE tabId = :tabId")
    suspend fun deleteTasksByTabId(tabId: Int)

    @Query("SELECT * FROM tasks WHERE tabId = :tabId ORDER BY createdAt DESC")
    fun getTasksByTabId(tabId: Int): Flow<List<Task>>

}
