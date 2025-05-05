package com.example.tamo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTabDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var tabName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "新しいタブを追加",
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
                },
        text = {
            TextField(
                value = tabName,
                onValueChange = { tabName = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray
                )
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (tabName.isNotBlank()) {
                    onConfirm(tabName)
                }
            },
                    colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6D1F),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(3.dp)
            ) {
                Text("作成")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6D1F),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(3.dp)) {
                Text("キャンセル")
            }
        }
    )
}
