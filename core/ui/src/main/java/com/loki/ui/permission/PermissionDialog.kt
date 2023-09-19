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
    permissions: List<String>,
    permissionToRequest: List<String>? = null,
    permissionRationale: Map<String, String>,
    snackbarHostState: SnackbarHostState,
    permissionAction: (Map<String,PermissionAction>) -> Unit
) {

    val grantedPermissions = mutableMapOf<String, PermissionAction>()
    val permissionToRequestSet = permissionToRequest?.toSet()

    permissions.forEach { permission ->

        if (permissionToRequestSet == null || permissionToRequestSet.contains(permission)) {
            val isPermissionGranted = checkIfPermissionGranted(context, permission)

            if (isPermissionGranted) {
                grantedPermissions[permission] = PermissionAction.PermissionGranted
            } else {

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->

                    val action = if (isGranted) {
                        PermissionAction.PermissionGranted
                    } else {
                        PermissionAction.PermissionDenied
                    }

                    grantedPermissions[permission] = action

                    //checks if we all collected responses for all permission
                    if (grantedPermissions.size == permissions.size) {
                        permissionAction(grantedPermissions)
                    }
                }

                val showPermissionRationale = shouldShowPermissionRationale(context, permission)

                if (showPermissionRationale) {
                    LaunchedEffect(key1 = showPermissionRationale) {

                        val rationale = permissionRationale[permission]
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = rationale ?: "Permission Required",
                            actionLabel = "Grant Access",
                            duration = SnackbarDuration.Long
                        )

                        when (snackbarResult) {
                            SnackbarResult.Dismissed -> {
                                grantedPermissions[permission] = PermissionAction.PermissionDenied

                                // Check if we have collected responses for all permissions
                                if (grantedPermissions.size == permissions.size) {
                                    permissionAction(grantedPermissions)
                                }
                            }

                            SnackbarResult.ActionPerformed -> permissionLauncher.launch(permission)
                        }
                    }
                } else {
                    SideEffect {
                        permissionLauncher.launch(permission)
                    }
                }
            }
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