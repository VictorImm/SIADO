package com.example.siado.ui

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.siado.camerax.CameraConstants
import com.example.siado.camerax.CameraManager
import com.example.siado.databinding.ActivityCameraBinding
import com.example.siado.databinding.DialogCapturedBinding
import com.example.siado.ui.CustomDialog
import java.util.concurrent.ExecutorService

class CameraActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityCameraBinding

    // widgets
    private lateinit var btnCapture: Button
    private lateinit var bgDim: ImageView
    private lateinit var loading: ProgressBar

    // cameraX
    private lateinit var cameraView: PreviewView
    private lateinit var cameraManager: CameraManager

    companion object {
        var bitmapLiveData = MutableLiveData<Bitmap>()
    }

    private val dialog = CustomDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // init widget
        btnCapture = binding.btnCapture
        bgDim = binding.bgDim
        loading = binding.progressBar
        cameraView = binding.cameraView

        // init cameraX
        cameraManager = CameraManager(
            this,
            cameraView,
            this
        )
        cameraManager.startCamera()

        // btn capture
        btnCapture.setOnClickListener {
            Log.d("btnCapture", "clicked!")

            // take photo
            cameraManager.takePhoto()

            // launch progress bar
            bgDim.visibility = View.VISIBLE
            loading.visibility = View.VISIBLE

            // show custom dialog
            dialog.showDialog(this, this@CameraActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.stopCamera()
    }
}