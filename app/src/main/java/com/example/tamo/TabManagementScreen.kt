package com.example.tamo.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tamo.components.AddTabDialog
import com.example.tamo.components.DeleteTabDialog
import com.example.tamo.components.EditTabDialog
import com.example.tamo.components.TabItem
import com.example.tamo.data.Tab
import com.example.tamo.viewmodel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabManagementScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val tabs by viewModel.tabs.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var tabToDelete by remember { mutableStateOf<Tab?>(null) }
    var editingTab by remember { mutableStateOf<Tab?>(null) }
    var editedTabName by remember { mutableStateOf("") }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(0xFFFF6D1F)
        )
    }

    Scaffold(
        modifier = Modifier
            .background(Color(0xFFF6F6F6)),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("タブの管理", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "戻る", tint = Color.DarkGray)
                    }
                },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "追加", tint = Color.DarkGray)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFF6D1F)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)

        ) {
            items(tabs) { tab ->
                TabItem(
                    tab = tab,
                    onDelete = { tabToDelete = it },
                    onEdit = { t ->
                        editingTab = t
                        editedTabName = t.name
                    }
                )
            }
        }

        if (tabToDelete != null) {
            DeleteTabDialog(
                tab = tabToDelete!!,
                onConfirm = {
                    viewModel.deleteTab(tabToDelete!!)
                    tabToDelete = null
                },
                onDismiss = { tabToDelete = null }
            )
        }
    }

    if (showDialog) {
        AddTabDialog(
            onConfirm = {
                viewModel.addTab(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    if (editingTab != null) {
        EditTabDialog(
            editingTab = editingTab!!,
            onConfirm = {
                viewModel.updateTab(it)
                editingTab = null
            },
            onDismiss = {
                editingTab = null
            }
        )
    }


}