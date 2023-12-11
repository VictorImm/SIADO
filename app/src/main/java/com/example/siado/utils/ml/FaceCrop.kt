package com.example.siado.utils.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceCrop(
    private val context: Context
) {
    // face detector
    private lateinit var detector: FaceDetector

    companion object {
        const val SCALING_FACTOR = 10
    }

    init {

        // real-time contour detection
        val realTimeFdo = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .build()

        // init face detector
        detector = FaceDetection.getClient(realTimeFdo)

    }

    fun analyzeBitmap(bitmap: Bitmap, callback: (List<Face>) -> Unit) {
        // make image smaller to do processing faster
        val smallerBitmap = Bitmap.createScaledBitmap(
            bitmap,
            bitmap.width / SCALING_FACTOR,
            bitmap.height / SCALING_FACTOR,
            false
        )

        // input image for analyzing
        val inputImage = InputImage.fromBitmap(smallerBitmap, 0)

        // starting detection
        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                // task completed
                Toast.makeText(
                    context,
                    "Face detected",
                    Toast.LENGTH_SHORT
                ).show()

                for (face in faces) {
                    val rect = face.boundingBox

                    Log.d("FaceDetection", "Face Rect: $rect")
                    rect.set(
                        rect.left * SCALING_FACTOR,
                        rect.top * (SCALING_FACTOR - 1),
                        rect.right * (SCALING_FACTOR),
                        rect.bottom * SCALING_FACTOR + 98
                    )
                }

                callback(faces)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Failed die to: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun cropDetectedFace(bitmap: Bitmap, faces: List<Face>): Bitmap? {        // face was detected, now get cropped image
        val rect = faces[0].boundingBox

        val x = Math.max(rect.left, 0)
        val y = Math.max(rect.top, 0)

        val width = rect.width()
        val height = rect.height()

        // cropped bitmap
        val croppedBitmap = Bitmap.createBitmap(
            bitmap,
            x,
            y,
            if (x+width > bitmap.width) bitmap.width-x else width,
            if (y+height > bitmap.height) bitmap.height-y else height
        )

        // set cropped bitmap to imageView
        return croppedBitmap
    }
}