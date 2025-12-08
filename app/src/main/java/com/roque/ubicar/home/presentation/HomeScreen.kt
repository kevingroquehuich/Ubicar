package com.roque.ubicar.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.roque.ubicar.home.presentation.components.HomeMap

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel = hiltViewModel()
) {

    val state = viewmodel.state

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            HomeMap(
                currentLocation = null,
                carLocation = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}