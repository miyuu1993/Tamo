package com.example.tamo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tamo.onboarding.OnboardingScreen
import com.example.tamo.ui.screen.TabManagementScreen


@Composable
fun TamoApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") { OnboardingScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("tab_management") { TabManagementScreen(navController) }
    }
}
