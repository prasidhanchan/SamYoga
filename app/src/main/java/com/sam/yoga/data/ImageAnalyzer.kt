package com.sam.yoga.data

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.sam.yoga.domain.Util.SIZE
import com.sam.yoga.domain.Util.centerCrop
import com.sam.yoga.domain.models.Classification
import com.sam.yoga.domain.repository.YogaPoseClassifier

class ImageAnalyzer(
    private val classifier: YogaPoseClassifier,
    private val onResult: (List<Classification>) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0) {
            val rotation = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(SIZE, SIZE)

            val results = classifier.classify(
                bitmap = bitmap,
                rotation = rotation
            )
            onResult(results)
        }
        frameSkipCounter += 1
        image.close()
    }
}