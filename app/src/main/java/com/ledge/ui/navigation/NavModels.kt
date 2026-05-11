package com.ledge.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(

    val route: String
) {

    data object Dashboard :
        Routes("dashboard")

    data object Transactions :
        Routes("transactions")

    data object Add :
        Routes("add")

    data object Reports :
        Routes("reports")

    object Search : Routes("search")

    data object Settings :
        Routes("settings")

    data object Budget :
        Routes("budget")
}

data class BottomNavItem(

    val title: String,

    val icon: ImageVector,

    val route: String
)

val bottomNavItems = listOf(

    BottomNavItem(

        title = "Home",

        icon =
            Icons.Outlined.Home,

        route =
            Routes.Dashboard.route
    ),

    BottomNavItem(

        title = "Transactions",

        icon =

            Icons.AutoMirrored
                .Outlined
                .ReceiptLong,

        route =
            Routes.Transactions.route
    ),

    BottomNavItem(

        title = "Add",

        icon =
            Icons.Outlined.Add,

        route =
            Routes.Add.route
    ),

    BottomNavItem(

        title = "Reports",

        icon =
            Icons.Outlined.Analytics,

        route =
            Routes.Reports.route
    ),

    BottomNavItem(

        title = "Search",

        icon =
            Icons.Outlined.Search,

        route =
            Routes.Search.route
    ),

    BottomNavItem(

        title = "Settings",

        icon =
            Icons.Outlined.Settings,

        route =
            Routes.Settings.route
    )
)