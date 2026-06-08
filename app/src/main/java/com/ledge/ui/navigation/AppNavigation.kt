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
        }
    ) { paddingValues -> // 🔔 This tracks the exact layout area around your floating menu bar

        NavHost(
            navController = navController,
            startDestination = Routes.Dashboard.route,
            modifier = Modifier
            // 🔔 REMOVED global padding here so that the screen background runs edge-to-edge
            // behind the transparent margins of your floating bottom nav bar.
        ) {
            composable(route = Routes.Dashboard.route) {
                // 🔔 Forwarding paddingValues directly down into the Screen content
                DashboardScreen(paddingValues = paddingValues)
            }

            composable(route = Routes.Transactions.route) {
                com.ledge.ui.transactions.TransactionsScreen(paddingValues = paddingValues)
            }

            composable(route = Routes.Add.route) {
                com.ledge.ui.add.AddTransactionScreen()
            }

            composable(route = Routes.Reports.route) {
                // 🔔 Forwarding paddingValues to your Reports tab list container
                ReportsScreen(paddingValues = paddingValues)
            }

            composable(route = "search") {
                SearchScreen()
            }

            composable(route = Routes.Settings.route) {
                SettingsScreen(
                    onOpenBudgetSettings = {
                        navController.navigate(Routes.Budget.route)
                    }
                )
            }

            composable(route = Routes.Budget.route) {
                com.ledge.ui.budget.BudgetScreen()
            }
        }
    }
}