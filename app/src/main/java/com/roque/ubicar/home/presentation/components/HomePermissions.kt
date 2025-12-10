package com.roque.ubicar.home.presentation.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomePermissions(
    permissions: List<String>,
    onPermissionResult: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.allPermissionsGranted) {
        onPermissionResult(true)
    } else if (permissionsState.shouldShowRationale){
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {},
            confirmButton = {
                HomeButton(
                    onClick = { permissionsState.launchMultiplePermissionRequest() },
                    text = "OK"
                )
            },
            title = {
                Text(text = "Permission Required")
            },
            text = {
                Text(text = "We need your location for the app work correctly.")
            }
        )
    }else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current
            Text(text = "We need your location for the app work correctly.")
            HomeButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                },
                text = "Open App Settings"
            )
        }
    }

}