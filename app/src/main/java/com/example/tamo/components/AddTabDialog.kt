package com.example.tamo.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddTabDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var tabName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新しいタブを追加") },
        text = {
            TextField(
                value = tabName,
                onValueChange = { tabName = it },
                label = { Text("タブ名") }
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (tabName.isNotBlank()) {
                    onConfirm(tabName)
                }
            }) {
                Text("作成")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}
