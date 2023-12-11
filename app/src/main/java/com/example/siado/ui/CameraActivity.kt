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
import com.example.siado.data.application.Application
import com.example.siado.utils.camerax.CameraManager
import com.example.siado.databinding.ActivityCameraBinding
import com.example.siado.ui.dialog.TrueDialog
import com.example.siado.utils.GetTimeNow
import com.example.siado.data.user.viewmodel.UserViewModel
import com.example.siado.data.user.viewmodel.UserViewModelFactory
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

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
        var captureStatusLiveData = MutableLiveData<Int>()
        var dialogStatusLiveData = MutableLiveData<Int>()
    }

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            (application as Application).database.userDao()
        )
    }

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
        initCamera()

        // btn capture
        btnCapture.setOnClickListener {
            Log.d("btnCapture", "clicked!")

            // get dateTime
            val dateTime = GetTimeNow.getDateToday()

            // take photo
            cameraManager.takePhoto(
                viewModel,
                dateTime,
                this
            )

            // launch progress bar
            bgDim.visibility = View.VISIBLE
            loading.visibility = View.VISIBLE

            captureStatusLiveData.observe(this, Observer {  status ->
                if (status == 0) {
                    bgDim.visibility = View.GONE
                    loading.visibility = View.GONE

                    captureStatusLiveData.postValue(1)

                    // reset camera
                    cameraManager.stopCamera()
                    initCamera()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.stopCamera()
    }

    private fun initCamera() {
        // init cameraX
        cameraManager = CameraManager(
            this,
            cameraView,
            this
        )
        cameraManager.startCamera()

        // TODO: Apakah akan membebani hrdware jika membuat terlalu banyak threads?
        // finish activity when 60s idle
        dialogStatusLiveData.observe(this@CameraActivity, Observer {
            if (dialogStatusLiveData.value == 0) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(60000)

                    if (dialogStatusLiveData.value == 0) {
                        finish()
                    }
                }
            }
        })
    }
}