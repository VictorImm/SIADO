package com.example.siado.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.siado.data.application.Application
import com.example.siado.utils.camerax.CameraConstants
import com.example.siado.databinding.ActivityMainBinding
import com.example.siado.utils.CameraPermissionHandler
import com.example.siado.viewmodel.UserViewModel
import com.example.siado.viewmodel.UserViewModelFactory

class MainActivity : AppCompatActivity() {

    companion object {
        const val databaseUrl = "https://siado-temp-default-rtdb.asia-southeast1.firebasedatabase.app/"
    }

    // binding
    private lateinit var binding: ActivityMainBinding

    // widgets
    private lateinit var btnPresent: Button
    private lateinit var btnClear: Button

    // viewModel
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            (application as Application).database.userDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // init widgets
        btnPresent = binding.btnPresent
        btnPresent.isEnabled = false
        btnPresent.setOnClickListener {
            CameraActivity.dialogStatusLiveData.postValue(0)

            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
        }

        btnClear = binding.btnClear
        btnClear.setOnClickListener {
            viewModel.clear()
        }

        // check if all permissions is granted
        if (CameraPermissionHandler.allPermissionsGranted(baseContext)) {
            btnPresent.isEnabled = true
        } else {
            // if not granted yet, request for permissions
            ActivityCompat.requestPermissions(
                this,
                CameraConstants.REQUIRED_PERMISSIONS,
                CameraConstants.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // check if requestCode is same as what we want to access
        if (requestCode == CameraConstants.REQUEST_CODE_PERMISSIONS) {
            if (CameraPermissionHandler.allPermissionsGranted(baseContext)) {
                // able to click the button
                btnPresent.isEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "Permission not yet granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}