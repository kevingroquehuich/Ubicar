package com.roque.ubicar.navigation

sealed class NavigationRoute(val route: String){
    data object LoginScreen : NavigationRoute("Login")
    data object HomeScreen : NavigationRoute("Home")
}