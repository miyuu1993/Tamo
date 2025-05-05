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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    selectedTabId: Int,
    onDismiss: () -> Unit,
    onConfirm: (title: String, remindEnabled: Boolean, remindTimeMillis: Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var remindEnabled by remember { mutableStateOf(false) }
    var remindTimeMillis by remember { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "タスク新規作成",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = Color.DarkGray
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.DarkGray,
                        unfocusedBorderColor = Color.DarkGray
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "リマインド",
                        color = Color.DarkGray
                    )
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

                    val timePickerState = rememberTimePickerState()

                    val context = LocalContext.current

                    var hour by remember { mutableStateOf(0) }
                    var minute by remember { mutableStateOf(0) }

                    // 選択された時間を表示するText
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
                        Text(
                            text = "通知時間   "+String.format("%02d:%02d", hour, minute),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, remindEnabled, remindTimeMillis) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6D1F),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(3.dp)
            ) {
                Text(text = "OK")
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

@Preview(showBackground = true)
@Composable
fun AddTaskDialogPreview() {
    AddTaskDialog(
        selectedTabId = 1,
        onDismiss = {},
        onConfirm = { _, _, _ -> }
    )
}