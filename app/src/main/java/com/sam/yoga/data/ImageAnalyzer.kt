package com.sam.yoga.data

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.sam.yoga.domain.Util.SIZE
import com.sam.yoga.domain.Util.centerCrop
import com.sam.yoga.domain.models.Classification
import com.sam.yoga.domain.repository.YogaPoseClassifier

class ImageAnalyzer(
    private val classifier: YogaPoseClassifier,
    private val onResult: (List<Classification>) -> Unit,
    private val onPoseChange: (Pose, Int, Int) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0
    private val poseDetectorOptions = PoseDetectorOptions.Builder().build()
    private val poseDetector = PoseDetection.getClient(poseDetectorOptions)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val imageWidth = imageProxy.width
        val imageHeight = imageProxy.height
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val rotation = imageProxy.imageInfo.rotationDegrees
        val bitmap = imageProxy
            .toBitmap()
            .centerCrop(SIZE, SIZE)

        if (frameSkipCounter % 60 == 0) {
            poseDetector.process(inputImage)
                .addOnSuccessListener { pose ->
                    onPoseChange(pose, imageWidth, imageHeight)
                }
                .addOnFailureListener { error ->
                    Log.d("ERRORRR", "analyze: $error")
                }

            val results = classifier.classify(
                bitmap = bitmap,
                rotation = rotation
            )
            onResult(results)
        }
        frameSkipCounter += 1
        imageProxy.close()
    }
}