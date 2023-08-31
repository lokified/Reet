package com.loki.ui.permission

sealed class PermissionAction {
    object PermissionGranted: PermissionAction()
    object PermissionDenied: PermissionAction()
    object PermissionAlreadyGranted: PermissionAction()
}