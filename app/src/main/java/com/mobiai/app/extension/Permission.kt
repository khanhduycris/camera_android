package com.mobiai.app.extension

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Permission {
     fun cameraPermissionWasGiven(context : Context) : Boolean {
        if (ContextCompat.checkSelfPermission(context,  android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }

        return false
    }

    fun hasWriteStoragePermission(context: Context ) : Boolean{
        if (ContextCompat.checkSelfPermission(context,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }

        return false
    }
}