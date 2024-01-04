package com.example.siado.utils.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
//import com.example.siado.ml.FaceModel
import com.example.siado.ui.MainActivity.Companion.interpreter
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object PhotoProcessor {

    private val label = listOf(
        "Amri Muhaimin, S.Stat., M.Stat., M.S",
        "Aviolla Terza Damaliana, S.Si, M.Stat",
        "Dr.Eng.Ir.Dwi Arman Prasetya.,ST.,MT.,IPU., Asean. Eng",
        "Kartika Maulida Hindrayani S.Kom, M.Kom', 
        'Mhs_Alex",
        "Mhs_Ardhy",
        "Mhs_Hanif",
        "Mhs_Mikio",
        "Mhs_Rafka",
        "Mhs_Zahy",
        "Tresna Maulana Fahrudin, S.ST., M.T",
        "Trimono, S.Si., M.Si",
        "Wahyu Syaifullah Jauharis Saputra, S.Kom., M.Kom"
    )
    fun classify(
        bitmapImage: Bitmap,
        context: Context
    ): String {
        return firebaseModel(bitmapImage, context)
    }

    private fun FloatArray.argMax(): Int {
        var maxIndex = 0
        var maxValue = this[maxIndex]
        for (i in 1 until this.size) {
            if (this[i] > maxValue) {
                maxIndex = i
                maxValue = this[i]
            }
        }
        return maxIndex
    }

//    private fun localModel(
//        bitmapImage: Bitmap,
//        context: Context
//    ): String {
//        // scale down bitmap
//        val scaledBitmap = Bitmap.createScaledBitmap(bitmapImage, 244, 244, true)
//
//        // instantiate model
//        val model = FaceModel.newInstance(context)
//
//        // create inputs for reference
//        val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
//
//        val input = ByteBuffer.allocateDirect(224*224*3*4).order(ByteOrder.nativeOrder())
//        for (y in 0 until 224) {
//            for (x in 0 until 224) {
//                val px = scaledBitmap.getPixel(x, y)
//
//                // Get channel values from the pixel value.
//                val r = Color.red(px)
//                val g = Color.green(px)
//                val b = Color.blue(px)
//
//                // Normalize channel values to [-1.0, 1.0]. This requirement depends on the model.
//                // For example, some models might require values to be normalized to the range
//                // [0.0, 1.0] instead.
//                val rf = (r - 127) / 255f
//                val gf = (g - 127) / 255f
//                val bf = (b - 127) / 255f
//
//                input.putFloat(rf)
//                input.putFloat(gf)
//                input.putFloat(bf)
//            }
//        }
//
//        inputFeature.loadBuffer(input)
//
//        // Runs model inference and gets result.
//        val outputs = model.process(inputFeature)
//        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//
//        val label = listOf(
//            "afdhal", "alex", "anip", "ardhy", "mikio", "rafka", "tresna", "wahyu","zahy"
//        )
//
//        val pred_float = outputFeature0.floatArray
//
//        for (i in pred_float) {
//            // TODO: cek modus, return kalau sesuai
//            Log.d("pred_result", i.toString())
//        }
//        val pred = pred_float.argMax()
//
//        // Releases model resources if no longer used.
//        model.close()
//
//        return label[pred]
//    }

    private fun firebaseModel(
        bitmapImage: Bitmap,
        context: Context
    ): String {
        // scale down bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmapImage, 244, 244, true)

        // Prepare input data
        val input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder())
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val px = scaledBitmap.getPixel(x, y)
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)

                val rf = (r - 127) / 255f
                val gf = (g - 127) / 255f
                val bf = (b - 127) / 255f

                input.putFloat(rf)
                input.putFloat(gf)
                input.putFloat(bf)
            }
        }

        val bufferSize = 1000 * java.lang.Float.SIZE / java.lang.Byte.SIZE
        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())

        interpreter?.run(input, modelOutput)

        modelOutput.rewind()
        val probabilities = modelOutput.asFloatBuffer()

        // Process inference result
        val pred_float = FloatArray(label.size)
        probabilities.get(pred_float)

        val pred = pred_float.argMax()

        return label[pred]
    }
}