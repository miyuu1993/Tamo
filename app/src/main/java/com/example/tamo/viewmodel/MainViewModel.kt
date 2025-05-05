package com.example.tamo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamo.data.MeigenRepository
import com.example.tamo.data.Tab
import com.example.tamo.data.TabRepository
import com.example.tamo.data.Task
import com.example.tamo.data.TaskRepository
import com.example.tamo.util.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tabRepository: TabRepository,
    private val meigenRepository: MeigenRepository
) : ViewModel() {

    private val _selectedTabId = MutableStateFlow<Int?>(null)
    val selectedTabId: StateFlow<Int?> = _selectedTabId

    val tabs: StateFlow<List<Tab>> = tabRepository.getTabs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // タスク一覧取得 - タブIDに基づいて直接フィルタリングせず全タスクを取得
    val tasks: StateFlow<List<Task>> = taskRepository.getTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _quoteText = MutableStateFlow<String?>(null)
    val quoteText: StateFlow<String?> = _quoteText

    private val _quoteAuthor = MutableStateFlow<String?>(null)
    val quoteAuthor: StateFlow<String?> = _quoteAuthor

    init {

        viewModelScope.launch {
            tabs.collect { tabList ->
                val currentId = _selectedTabId.value
                val idExists = tabList.any { it.id == currentId }

                if ((currentId == null || !idExists) && tabList.isNotEmpty()) {
                    _selectedTabId.value = tabList.first().id
                    android.util.Log.d(
                        "TamoApp",
                        "❗️存在しないIDを補正 → 新しく選ばれたID: ${tabList.first().id}"
                    )
                }
            }
        }
            viewModelScope.launch {
                val result = meigenRepository.fetchMeigen()
                _quoteText.value = result?.meigen
                _quoteAuthor.value = result?.auther
            }
        }


    fun selectTab(id: Int) {
        _selectedTabId.value = id
    }

    // タスク追加＋通知登録
    fun addTask(task: Task, context: Context) {
        viewModelScope.launch {
            val newId = taskRepository.insertTask(task)
            android.util.Log.d("TamoApp", "Task added with ID: $newId")
            task.remindAt?.let {
                if (it > 0) {
                    NotificationHelper.scheduleReminder(context, newId, task.title, it)
                }
            }
        }
    }

    // タスク編集＋通知再設定
    fun updateTask(task: Task, context: Context) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
            NotificationHelper.cancelReminder(context, task.id)
            if (!task.isCompleted) {
                task.remindAt?.let {
                    if (it > 0) {
                        NotificationHelper.scheduleReminder(context, task.id, task.title, it)
                    }
                }
            }
        }
    }

    // タスク削除
    fun deleteCompletedTasks(context: Context) {
        viewModelScope.launch {
            val completed = taskRepository.getTasks().first().filter { it.isCompleted }
            completed.forEach { task ->
                NotificationHelper.cancelReminder(context, task.id)
                taskRepository.deleteTask(task)
            }
        }
    }

    // タブ追加
    fun addTab(name: String) {
        viewModelScope.launch {
            val tab = Tab(name = name)
            val id = tabRepository.insertTab(tab)
            _selectedTabId.value = id
            android.util.Log.d("TamoApp", "Tab added with ID: $id")
        }
    }

    // タブ編集
    fun updateTab(tab: Tab) {
        viewModelScope.launch {
            tabRepository.updateTab(tab)
        }
    }

    // タブ削除
    fun deleteTab(tab: Tab, context: Context) {
        viewModelScope.launch {
            // タブに関連するタスクを削除
            val tabTasks = taskRepository.getTasksByTab(tab.id).first()
            tabTasks.forEach { task ->
                NotificationHelper.cancelReminder(context, task.id)
                taskRepository.deleteTask(task)
            }

            // タブを削除
            tabRepository.deleteTab(tab)
            android.util.Log.d("TamoApp", "Tab deleted: ${tab.id}")

            // 削除後、別のタブを選択
            val remainingTabs = tabRepository.getTabs().first()
            val selectedId = if (remainingTabs.isNotEmpty()) {
                remainingTabs.first().id
            } else {
                tabRepository.getTabs().first().firstOrNull()?.id
            }
            _selectedTabId.value = selectedId
        }
    }
}
