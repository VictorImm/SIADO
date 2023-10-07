package com.example.siado.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.siado.camerax.CameraConstants

object CameraPermissionHandler {

    fun allPermissionsGranted(baseContext: Context) =
        // check for all permissions
        CameraConstants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

}