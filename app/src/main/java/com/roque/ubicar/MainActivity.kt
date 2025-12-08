package com.roque.ubicar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.roque.ubicar.navigation.NavigationHost
import com.roque.ubicar.navigation.NavigationRoute
import com.roque.ubicar.ui.theme.UbiCarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UbiCarTheme {
                val navHostController = rememberNavController()
                val startDestination = getStartDestination()
                NavigationHost(navHostController = navHostController, startDestination = startDestination)
            }
        }
    }

    private fun getStartDestination(): NavigationRoute {
        return if (viewModel.isLoggedIn) NavigationRoute.HomeScreen else NavigationRoute.LoginScreen
    }
}