package com.example.tamo

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tamo.data.OnboardingPreferences
import com.example.tamo.onboarding.OnboardingScreen
import com.example.tamo.ui.screen.TabManagementScreen
import com.example.tamo.viewmodel.MainViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val TAG = "AppNavigation"

@Composable
fun AppNavigation(context: Context) {
    val navController = rememberNavController()
    var onboardingState by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            onboardingState = OnboardingPreferences
                .readOnboardingCompleted(context.applicationContext)
                .catch {
                    Log.e(TAG, "オンボーディング状態の読み取りに失敗", it)
                    emit(false)
                }
                .first() // ← Flowをcollectではなく1回だけ取得！
        } catch (e: Exception) {
            Log.e(TAG, "読み取り中に予期しない例外", e)
            onboardingState = false
            isLoading = false
        }
    }


    when (onboardingState) {
        null -> SplashContent()

        else -> {
            val startDestination = if (onboardingState == true) "main" else "onboarding"

            NavHost(navController = navController, startDestination = startDestination) {
                composable("onboarding") {
                    OnboardingScreen(
                        navController = navController,
                        onFinish = {
                            scope.launch {
                                val success = OnboardingPreferences.saveOnboardingCompleted(context.applicationContext)
                                if (success) {
                                    viewModel.addTab("やること")
                                    onboardingState = true
                                    navController.navigate("main") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                }
                            }
                        }
                    )
                }

                composable("main") {
                    MainScreen(navController = navController)
                }

                composable("tab_management") {
                    TabManagementScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun SplashContent() {
    // スプラッシュ画面と同じデザインをComposeで実装
    // activity_splash.xmlと同じ見た目になるようにする
    Surface(modifier = Modifier.fillMaxSize()) {
        // スプラッシュ画面のロゴやブランディングなどを配置
        // インジケーターは表示しない、またはブランドの一部として表示

    }
}
