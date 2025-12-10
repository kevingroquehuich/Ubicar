package com.roque.ubicar.authentication.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.roque.ubicar.R
import com.roque.ubicar.authentication.presentation.components.LoginWithGoogleButton

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(state.loginStatus) {
        if (state.loginStatus == LoginStatus.LOGGED_IN) onLoggedIn()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Image(
                modifier = Modifier.fillMaxSize().offset(y = (-32).dp),
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
            )
            LoginWithGoogleButton(
                onClick = { viewModel.onEvent(LoginEvent.SignIn(context)) },
                text = stringResource(R.string.continue_with_google),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}