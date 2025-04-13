package com.example.tamo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun SplashScreen() {
    val systemUiController = rememberSystemUiController()

    // ステータスバーとナビゲーションバーを非表示
    LaunchedEffect(Unit) {
        systemUiController.isStatusBarVisible = false
        systemUiController.isNavigationBarVisible = false
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFF6D1F)
    ){
        Box(contentAlignment = Alignment.Center){
            Text(
                text = "Tamo",
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}