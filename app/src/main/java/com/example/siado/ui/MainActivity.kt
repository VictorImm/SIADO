package com.example.siado.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.siado.utils.camerax.CameraConstants
import com.example.siado.databinding.ActivityMainBinding
import com.example.siado.utils.CameraPermissionHandler

class MainActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityMainBinding

    // widgets
    private lateinit var btnPresent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // init widgets
        btnPresent = binding.btnPresent
        btnPresent.isEnabled = false
        btnPresent.setOnClickListener {
            CameraActivity.bitmapLiveData.postValue(null)
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
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