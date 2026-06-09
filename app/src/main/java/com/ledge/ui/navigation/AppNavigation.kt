package com.ledge.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ledge.ui.components.BottomBar
import com.ledge.ui.dashboard.DashboardScreen
import com.ledge.ui.reports.ReportsScreen
import com.ledge.ui.search.SearchScreen
import com.ledge.ui.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Routes.Dashboard.route,
            modifier = Modifier
        ) {
            composable(route = Routes.Dashboard.route) {
                DashboardScreen(paddingValues = paddingValues)
            }

            composable(route = Routes.Add.route) {
                com.ledge.ui.add.AddTransactionScreen()
            }

            composable(route = Routes.Reports.route) {
                ReportsScreen(paddingValues = paddingValues)
            }

            composable(route = Routes.Search.route) {
                SearchScreen(paddingValues = paddingValues)
            }

            composable(route = Routes.Settings.route) {
                SettingsScreen(
                    onOpenBudgetSettings = {
                        navController.navigate(Routes.Budget.route)
                    })
            }

            composable(route = Routes.Budget.route) {
                com.ledge.ui.budget.BudgetScreen()
            }
        }
    }
}