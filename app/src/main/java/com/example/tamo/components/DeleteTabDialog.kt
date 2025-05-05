package com.example.tamo.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tamo.data.Tab

@Composable
fun DeleteTabDialog(
    tab: Tab,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(
            "タブを削除しますか？",
            color = Color.DarkGray
        ) },
        text = { Text(
            "このタブに含まれるタスクもすべて削除されます。",
            color = Color.DarkGray
        ) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors
                (
                containerColor = Color(0xFFFF6D1F),
                contentColor = Color.White
                ),
                shape = RoundedCornerShape(3.dp)) {
                Text("削除")
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
                shape = RoundedCornerShape(3.dp)
                ) {
                Text("キャンセル")
            }
        }
    )
}
