package app.initcn.ledge.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.initcn.ledge.ui.components.BottomBar
import app.initcn.ledge.ui.dashboard.DashboardScreen
import app.initcn.ledge.ui.reports.ReportsScreen
import app.initcn.ledge.ui.settings.SettingsScreen
import app.initcn.ledge.ui.transactions.TransactionsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Routes.Dashboard.route,
            modifier = Modifier
        ) {
            // 1. DASHBOARD
            composable(route = Routes.Dashboard.route) {
                DashboardScreen(paddingValues = paddingValues)
            }

            // 2. ADD TRANSACTION
            composable(route = Routes.Add.route) {
                app.initcn.ledge.ui.add.AddTransactionScreen(paddingValues = paddingValues)
            }

            // 3. REPORTS
            composable(route = Routes.Reports.route) {
                ReportsScreen(paddingValues = paddingValues)
            }

            // 4. HISTORY / SEARCH
            composable(route = Routes.Search.route) {
                TransactionsScreen(paddingValues = paddingValues)
            }

            // 5. SETTINGS
            composable(route = Routes.Settings.route) {
                SettingsScreen(
                    paddingValues = paddingValues,
                    onOpenBudgetSettings = {
                        navController.navigate(Routes.Budget.route)
                    }
                )
            }

            // 6. BUDGETS MANAGEMENT
            composable(route = Routes.Budget.route) {
                app.initcn.ledge.ui.budget.BudgetScreen()
            }
        }
    }
}