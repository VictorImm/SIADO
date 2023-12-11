package com.example.siado.utils.camerax

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.siado.data.DateTime
import com.example.siado.ml.FaceModel
import com.example.siado.ui.CameraActivity
import com.example.siado.utils.BitmapRotator
import com.example.siado.utils.ml.FaceCrop
import com.example.siado.utils.ml.PhotoProcessor
import com.example.siado.data.user.viewmodel.UserViewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val context: Context,
    private val finderView: PreviewView,
    private val lifecycleOwner: LifecycleOwner
) {
    // camera preview properties
    private var preview: Preview? = null

    // cameraX properties
    private lateinit var cameraExecutor: ExecutorService
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    init {
        createNewExecutor()
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun cameraConfigCapture() {
        try {
            // reset binder
            cameraProvider?.unbindAll()

            // bind to a new lifecycle
            cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )

            // build preview
            preview?.setSurfaceProvider(
                finderView.surfaceProvider
            )

        } catch (e: Exception) {
            Log.e("CameraManager", "Use case binding failed", e)
        }
    }

    fun startCamera() {
        // create cameraProvider temporary variable
        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(context)

        // initialize image capture
        imageCapture = ImageCapture
            .Builder()
            .build()

        // set listener to cameraProvider temporary variable
        cameraProviderFuture.addListener({
            // cast cameraProvider temporary to cameraProvider global
            cameraProvider = cameraProviderFuture.get()

            // build preview
            preview = Preview
                .Builder()
                .build()

            cameraConfigCapture()

        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(
        viewModel: UserViewModel,
        dateTime: DateTime,
        context: Context
    ) {

        imageCapture?.takePicture(
            ContextCompat.getMainExecutor(context),
            object: OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    // save image to buffer
                    val buffer: ByteBuffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)

                    // change buffer into bitmap
                    val bitmapImage = BitmapFactory
                        .decodeByteArray(
                            bytes,
                            0,
                            bytes.size,
                            null
                        )

                    if (bitmapImage != null) {
                        val rotatedImage = BitmapRotator.rotate(bitmapImage)

                        // crop detected face
                        val faceCrop = FaceCrop(context)

                        faceCrop.analyzeBitmap(rotatedImage) { faces ->
                            val croppedBitmap = faceCrop.cropDetectedFace(rotatedImage, faces)

                            // use ML model to verify the user name
                            val name = verifyPhoto(context, croppedBitmap!!)

                            // use viewModel to insert into database
                            insertName(
                                viewModel,
                                croppedBitmap,
                                name,
                                dateTime,
                                context
                            )
                        }

                        // clear bg dim and loading
                        CameraActivity.captureStatusLiveData.postValue(0)

                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("takePhoto", "on error: ${exception.message}", exception)
                }
            }
        )
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    private fun faceCropper(
        bitmap: Bitmap,
        context: Context
    ): Bitmap? {
        val faceCrop = FaceCrop(context)

        var croppedBitmap: Bitmap? = null

        faceCrop.analyzeBitmap(bitmap) { faces ->
            croppedBitmap = faceCrop.cropDetectedFace(bitmap, faces)
        }

        return croppedBitmap
    }

    private fun insertName(
        viewModel: UserViewModel,
        image: Bitmap,
        name: String,
        dateTime: DateTime,
        context: Context
    ) {
        viewModel.present(
            image,
            name,
            dateTime,
            context
        )
    }

    private fun verifyPhoto(
        context: Context,
        bitmapImage: Bitmap
    ): String {
        return (PhotoProcessor.classify(bitmapImage, context))
    }

}