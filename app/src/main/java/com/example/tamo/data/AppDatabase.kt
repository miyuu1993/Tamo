package com.example.tamo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class,Tab::class], version = 2,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun tabDao(): TabDao
}
