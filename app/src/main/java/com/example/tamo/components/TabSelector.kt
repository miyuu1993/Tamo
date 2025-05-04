package com.example.tamo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tamo.data.Tab
import com.example.tamo.viewmodel.MainViewModel

@Composable
fun TabSelector(
    viewModel: MainViewModel = hiltViewModel(),
    onAddTabClicked: () -> Unit,
    onTabSelected: (Int) -> Unit,
    selectedTabId: Int?,
    onDeleteCompletedTasks: () -> Unit
) {
    val tabs by viewModel.tabs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF6D1F)) // オレンジ色
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "チェック済みタスクを一括削除",
                tint = Color.DarkGray,
                modifier = Modifier.clickable { onDeleteCompletedTasks() }
            )
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "タブ追加",
                tint = Color.DarkGray,
                modifier = Modifier.clickable { onAddTabClicked() }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // タブ一覧（横スクロール）
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(tabs) { tab ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { onTabSelected(tab.id) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tab.name,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (tab.id == selectedTabId) {
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .width(100.dp)
                                .background(Color.White)
                        )
                    }
                }
            }
        }
    }
}
