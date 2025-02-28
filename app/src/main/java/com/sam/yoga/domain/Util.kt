package com.sam.yoga.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.content.ContextCompat

object Util {

    const val SIZE = 321

    fun Bitmap.centerCrop(desiredWidth: Int, desiredHeight: Int): Bitmap {
        val xStart = (width - desiredWidth) / 2
        val yStart = (height - desiredHeight) / 2

        if(xStart < 0 || yStart < 0 || desiredWidth > width || desiredHeight > height) {
            throw IllegalArgumentException("Invalid arguments for center cropping")
        }

        return Bitmap.createBitmap(this, xStart, yStart, desiredWidth, desiredHeight)
    }

    fun isPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}