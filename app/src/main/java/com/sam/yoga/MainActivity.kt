package com.sam.yoga

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.sam.yoga.domain.Util.isPermissionGranted
import com.sam.yoga.domain.navigation.SamYogaNavigation
import com.sam.yoga.presentation.theme.SamYogaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isPermissionGranted(this)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }

        enableEdgeToEdge()
        setContent {
            SamYogaTheme {
                SamYogaNavigation()
            }
        }
    }
}