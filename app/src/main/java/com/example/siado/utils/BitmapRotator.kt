package com.example.siado.utils

import android.graphics.Bitmap
import android.graphics.Matrix

object BitmapRotator {

    fun rotate(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(-90f)

        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

}