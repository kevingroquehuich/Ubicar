package com.roque.ubicar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roque.ubicar.authentication.presentation.LoginScreen

@Composable
fun NavigationHost(
    navHostController: NavHostController,
    startDestination: NavigationRoute
) {
    NavHost(navController = navHostController, startDestination = startDestination.route) {
        composable(NavigationRoute.LoginScreen.route) {
            LoginScreen()
        }

        composable(NavigationRoute.HomeScreen.route) {  }
    }
}