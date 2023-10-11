package com.example.siado.utils.camerax

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.siado.ui.CameraActivity
import java.nio.ByteBuffer
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

    fun takePhoto() {

        imageCapture?.takePicture(
            ContextCompat.getMainExecutor(context),
            object: OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val buffer: ByteBuffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.capacity())
                    buffer.get(bytes)
                    val bitmapImage = BitmapFactory
                        .decodeByteArray(
                            bytes,
                            0,
                            bytes.size,
                            null
                        )

                    if (bitmapImage != null) {
                        // TODO: do something
                        CameraActivity.bitmapLiveData.postValue(bitmapImage)
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

}