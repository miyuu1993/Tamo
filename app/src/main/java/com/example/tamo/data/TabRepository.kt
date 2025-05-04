package com.example.tamo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TabRepository @Inject constructor(
    private val tabDao: TabDao,
    private val taskRepository: TaskRepository
) {
    fun getTabs(): Flow<List<Tab>> = tabDao.getAllTabs()

    suspend fun insertTab(tab: Tab):Int {
        return tabDao.insert(tab).toInt()
    }

    suspend fun updateTab(tab: Tab) = tabDao.update(tab)

    suspend fun deleteTab(tab: Tab) {
        taskRepository.deleteTasksByTab(tab.id)
        tabDao.delete(tab)
    }
}