package com.sam.yoga.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import com.sam.yoga.R
import com.sam.yoga.SamYogaApplication.Companion.getAppContext

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

    fun getCorrectionTips(poseName: String): List<String> {
        val context = getAppContext()
        return when(poseName) {
            context.getString(R.string.ardha_chandrasana) -> listOf(
                "Keep the chest open and lifted.",
                "Engage the quadriceps and straighten the standing leg.",
                "Focus on a fixed point to maintain balance.",
                "Rotate the top hip outward, stacking it over the bottom hip."
            )
            context.getString(R.string.adho_mukha_svanasana) -> listOf(
                "Keep the back straight, lengthening the spine.",
                "Relax shoulders away from ears.",
                "Press heels toward the ground (knees can be slightly bent if needed).",
                "Keep a slight micro-bend to prevent hyperextension."
            )
            context.getString(R.string.baddha_konasana) -> listOf(
                "Sit upright with a straight spine.",
                "Use props like blocks under the knees for support.",
                "Bring heels closer to the groin without strain."
            )
            context.getString(R.string.natarajasana) -> listOf(
                "Keep hips squared forward.",
                "Lift the chest while extending the raised leg.",
                "Keep the raised leg’s knee pointing down.",
                "Engage the core to maintain stability."
            )
            context.getString(R.string.trikonasana) -> listOf(
                "Keep shoulders aligned and open sideways.",
                "Keep a slight micro-bend to avoid strain.",
                "Keep the neck neutral or look up only if comfortable.",
                "Distribute weight evenly between both feet."
            )
            context.getString(R.string.utkata_konasana) -> listOf(
                "Keep knees aligned with toes.",
                "Engage the core to keep a neutral spine.",
                "Widen the stance for better stability.",
                "Keep shoulders relaxed and away from ears."
            )
            context.getString(R.string.veerabhadrasana) -> listOf(
                "Align knee over ankle.",
                "Keep the back foot firmly pressed into the mat.",
                "Keep the torso upright and strong.",
                "Keep arms actively reaching in opposite directions."
            )
            else -> listOf(
                "Keep foot on thigh or calf, not the knee.",
                "Keep both hips squared and aligned.",
                "Fix your gaze on a steady focal point for better balance.",
                "Keep shoulders relaxed and arms extended gracefully."
            )
        }
    }
}