package com.example.siado.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.camera.view.PreviewView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.siado.utils.camerax.CameraManager
import com.example.siado.databinding.ActivityCameraBinding
import com.example.siado.ui.dialog.TrueDialog
import com.example.siado.utils.GetTimeNow

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

    private val trueDialog = TrueDialog()

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

            // get dateTime
            val dateTime = GetTimeNow.getDateToday()

            // take photo
            cameraManager.takePhoto()

            // launch progress bar
            bgDim.visibility = View.VISIBLE
            loading.visibility = View.VISIBLE

            // show custom dialog
            bitmapLiveData.observe(this, Observer { bitmap ->
                if (bitmap != null) {
                    val name = "Victor"
                    val job = "Programmer SIADO"
                    // TODO:
                    //  use ML model to verify the user name
                    //  use viewModel to present
                    trueDialog.showDialog(
                        name,
                        job,
                        this,
                        this@CameraActivity
                    )

                    bgDim.visibility = View.GONE
                    loading.visibility = View.GONE
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.stopCamera()
    }
}