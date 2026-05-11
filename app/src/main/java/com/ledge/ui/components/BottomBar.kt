package com.ledge.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults

import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

import com.ledge.ui.navigation.bottomNavItems
import com.ledge.ui.theme.LedgeTheme

@Composable
fun BottomBar(

    navController: NavHostController

) {

    val backStackEntry =

        navController
            .currentBackStackEntryAsState()

    val currentRoute =

        backStackEntry
            .value
            ?.destination
            ?.route

    NavigationBar(

        modifier = Modifier
            .height(84.dp),

        tonalElevation = 0.dp,

        containerColor =

            LedgeTheme
                .surfaces
                .surface
    ) {

        bottomNavItems.forEach { item ->

            NavigationBarItem(

                selected =

                    currentRoute ==
                            item.route,

                onClick = {

                    navController.navigate(
                        item.route
                    ) {

                        popUpTo(

                            navController.graph
                                .startDestinationId

                        )

                        launchSingleTop = true
                    }
                },

                icon = {

                    Icon(

                        imageVector =
                            item.icon,

                        contentDescription =
                            item.title,

                        modifier = Modifier
                            .size(20.dp)
                    )
                },

                alwaysShowLabel = false,

                colors =

                    NavigationBarItemDefaults
                        .colors(

                            indicatorColor =
                                Color.Transparent
                        )
            )
        }
    }
}