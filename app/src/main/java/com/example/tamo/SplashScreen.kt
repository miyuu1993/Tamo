//削除予定

package com.example.tamo

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {
    val activity = LocalActivity.current as? MainActivity

    var visible by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        activity?.hideSystemBars()
        delay(800L)
        visible = false
        navController.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFF6D1F),
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = visible,
            enter = androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.fadeOut()
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Tamo",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }
    }
}