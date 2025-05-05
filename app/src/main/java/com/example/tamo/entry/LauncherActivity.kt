package com.example.tamo.entry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tamo.AppNavigation
import com.example.tamo.ui.theme.TamoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // スプラッシュ非表示にした状態でCompose開始
        setContent {
            TamoTheme {
                AppNavigation(context = this)
            }
        }
    }
}
