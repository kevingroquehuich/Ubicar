package com.roque.ubicar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.roque.ubicar.navigation.NavigationHost
import com.roque.ubicar.navigation.NavigationRoute
import com.roque.ubicar.ui.theme.UbiCarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UbiCarTheme {
                val navHostController = rememberNavController()
                val startDestination = NavigationRoute.LoginScreen
                NavigationHost(navHostController = navHostController, startDestination = startDestination)
            }
        }
    }
}