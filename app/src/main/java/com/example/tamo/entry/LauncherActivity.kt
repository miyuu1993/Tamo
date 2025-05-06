package com.example.tamo.entry

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tamo.AppNavigation
import com.example.tamo.ui.theme.TamoTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.SideEffect
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.app.AlarmManager
import android.content.Context

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestExactAlarmPermission(this)

        setContent {
            TamoTheme {
                AppNavigation(context = this)

                // 安全にバーを隠す
                SideEffect {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        window.insetsController?.let {
                            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                            it.systemBarsBehavior =
                                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        window.decorView.systemUiVisibility = (
                                View.SYSTEM_UI_FLAG_FULLSCREEN or
                                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                )
                    }
                }
            }
        }
    }
}

fun checkAndRequestExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        }
    }
}
