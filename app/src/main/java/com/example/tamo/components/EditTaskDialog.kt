package com.example.tamo.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tamo.data.Task


@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onUpdate: (Task) -> Unit,
) {
    var title by remember { mutableStateOf(task.title) }
    var remindEnabled by remember { mutableStateOf(task.remindAt != null) }
    var remindTimeMillis by remember { mutableStateOf(task.remindAt) }

    val context = LocalContext.current

    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }

    // すでに設定されている時間を表示用に変換
    LaunchedEffect(task.remindAt) {
        task.remindAt?.let {
            val time = java.util.Calendar.getInstance().apply { timeInMillis = it }
            hour = time.get(java.util.Calendar.HOUR_OF_DAY)
            minute = time.get(java.util.Calendar.MINUTE)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "タスクを編集",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "リマインド")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = remindEnabled,
                        onCheckedChange = { remindEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Color(0xFFFF6D1F),
                            uncheckedTrackColor = Color.Transparent
                        )
                    )
                }

                if (remindEnabled) {
                    TextButton(
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, selectedHour: Int, selectedMinute: Int ->
                                    // 時間が選ばれたら状態を更新
                                    hour = selectedHour
                                    minute = selectedMinute

                                    val now = System.currentTimeMillis()
                                    val calendar = java.util.Calendar.getInstance().apply {
                                        timeInMillis = now
                                        set(java.util.Calendar.HOUR_OF_DAY, hour)
                                        set(java.util.Calendar.MINUTE, minute)
                                        set(java.util.Calendar.SECOND, 0)
                                        set(java.util.Calendar.MILLISECOND, 0)
                                        if (timeInMillis <= now) {
                                            add(java.util.Calendar.DATE, 1) // 現在時刻より前なら翌日に設定
                                        }
                                    }
                                    remindTimeMillis = calendar.timeInMillis

                                },
                                hour,
                                minute,
                                true  // 24時間制
                            ).show()
                        }
                    ) {
                        Text(text = "通知時間 ${String.format("%02d:%02d", hour, minute)}")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdate(
                        task.copy(
                            title = title,
                            remindAt = if (remindEnabled) remindTimeMillis else null
                        )
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6D1F),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(3.dp)
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
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
