package com.sam.yoga.domain.repository

import android.graphics.Bitmap
import com.sam.yoga.domain.models.Classification

interface YogaPoseClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}