package com.example.tamo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    fun getTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task): Int {
        return taskDao.insert(task).toInt()
    }

    suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    suspend fun deleteTasksByTab(tabId: Int) = taskDao.deleteTasksByTabId(tabId)

    fun getTasksByTab(tabId: Int): Flow<List<Task>> = taskDao.getTasksByTabId(tabId)

}
