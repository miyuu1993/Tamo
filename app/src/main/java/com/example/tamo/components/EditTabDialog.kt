package com.example.tamo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tamo.data.Tab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTabDialog(
    editingTab: Tab,
    onConfirm: (Tab) -> Unit,
    onDismiss: () -> Unit
) {
    var editedTabName by remember { mutableStateOf(editingTab.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(
            "タブ名を編集",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        ) },
        text = {
            TextField(
                value = editedTabName,
                onValueChange = { editedTabName = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray
                )
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (editedTabName.isNotBlank()) {
                    onConfirm(editingTab.copy(name = editedTabName))
                }
            },
            colors = ButtonDefaults.buttonColors
            (
            containerColor = Color(0xFFFF6D1F),
            contentColor = Color.White
            ),
            shape = RoundedCornerShape(3.dp)) {
                Text("更新")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors
                (
                containerColor = Color(0xFFFF6D1F),
                contentColor = Color.White
                ),
                shape = RoundedCornerShape(3.dp)) {
                Text("キャンセル")
            }
        }
    )
}
