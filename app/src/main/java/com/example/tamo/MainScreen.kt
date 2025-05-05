package com.example.tamo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tamo.viewmodel.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tamo.components.AddTaskDialog
import com.example.tamo.components.EditTaskDialog
import com.example.tamo.components.QuoteBanner
import com.example.tamo.components.TabSelector
import com.example.tamo.components.TaskList
import com.example.tamo.data.Task
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    val quoteText by viewModel.quoteText.collectAsState()
    val quoteAuthor by viewModel.quoteAuthor.collectAsState()
    val selectedTabId by viewModel.selectedTabId.collectAsState()
    val tabs by viewModel.tabs.collectAsState()
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    val currentTabId = selectedTabId ?: 0
    val filteredTasks = tasks.filter { it.tabId == currentTabId }


    SideEffect {
        systemUiController.isStatusBarVisible = true
        systemUiController.isNavigationBarVisible = true
        systemUiController.setStatusBarColor(
            color = Color(0xFFFF6D1F)
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFFFF6D1F),
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "タスク追加",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(paddingValues)
        ) {
            TabSelector(
                selectedTabId = viewModel.selectedTabId.collectAsState().value,
                onTabSelected = { id -> viewModel.selectTab(id) },
                onAddTabClicked = { navController.navigate("tab_management") },
                onDeleteCompletedTasks = { viewModel.deleteCompletedTasks(context) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            QuoteBanner(text = quoteText, author = quoteAuthor)

            TaskList(
                tasks = filteredTasks,
                onItemClick = { task -> editingTask = task },
                onCheckChange = { task, checked ->
                    viewModel.updateTask(task.copy(isCompleted = checked), context)
                }
            )
        }

        if (showDialog) {
            AddTaskDialog(
                selectedTabId = selectedTabId ?: 0,
                onDismiss = { showDialog = false },
                onConfirm = { title, remindEnabled, remindTimeMillis ->
                    val currentTabId = selectedTabId ?: 0
                    android.util.Log.d("TamoApp", "Creating task with tabId: $currentTabId, title: $title")
                    viewModel.addTask(
                        Task(
                            title = title,
                            tabId = currentTabId,
                            remindAt = remindTimeMillis
                        ),
                        context = context
                    )
                    showDialog = false
                }
            )
        }

        editingTask?.let { task ->
            EditTaskDialog(
                task = task,
                onDismiss = { editingTask = null },
                onUpdate = { updatedTask ->
                    viewModel.updateTask(updatedTask, context)
                    editingTask = null
                }
            )
        }
    }
}