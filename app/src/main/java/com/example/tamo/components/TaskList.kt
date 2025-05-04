package com.example.tamo.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tamo.data.Task

@Composable
fun TaskList(
    tasks: List<Task>,
    onItemClick: (Task) -> Unit,
    onCheckChange: (Task, Boolean) -> Unit
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onCheckedChange = { checked -> onCheckChange(task, checked) },
                onClick = { onItemClick(task) }
            )
        }
    }
}
