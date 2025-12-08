package com.roque.ubicar.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.roque.ubicar.home.presentation.components.HomeButton
import com.roque.ubicar.home.presentation.components.HomeMap

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel = hiltViewModel()
) {

    val state = viewmodel.state

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeMap(
                currentLocation = state.currentLocation,
                carLocation = null,
                modifier = Modifier.fillMaxSize()
            )

            HomeButton(
                onClick = { /*TODO*/ },
                text = "Park Here",
                imageVector = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }

}