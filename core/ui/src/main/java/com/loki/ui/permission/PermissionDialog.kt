package com.loki.ui.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun PermissionDialog(
    context: Context,
    permission: String,
    permissionRationale: String,
    snackbarHostState: SnackbarHostState,
    permissionAction: (PermissionAction) -> Unit
) {

    val isPermissionGranted = checkIfPermissionGranted(context, permission)

    if (isPermissionGranted) {
        permissionAction(PermissionAction.PermissionAlreadyGranted)
        return
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionAction(PermissionAction.PermissionGranted)
        }
        else {
            permissionAction(PermissionAction.PermissionDenied)
        }
    }

    val showPermissionRationale = shouldShowPermissionRationale(context, permission)

    if (showPermissionRationale) {
        LaunchedEffect(key1 = showPermissionRationale ) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = permissionRationale,
                actionLabel = "Grant Access",
                duration = SnackbarDuration.Long

            )

            when(snackbarResult) {
                SnackbarResult.Dismissed -> permissionAction(PermissionAction.PermissionDenied)
                SnackbarResult.ActionPerformed -> permissionLauncher.launch(permission)
            }
        }
    }
    else {
        SideEffect {
            permissionLauncher.launch(permission)
        }
    }
}

fun checkIfPermissionGranted(context: Context, permission: String): Boolean {
    return (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED)
}

fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {

    val activity = context as Activity?
    if (activity == null)
        Log.d("Maps Util", "Activity is null")

    return ActivityCompat.shouldShowRequestPermissionRationale(
        activity!!,
        permission
    )
}