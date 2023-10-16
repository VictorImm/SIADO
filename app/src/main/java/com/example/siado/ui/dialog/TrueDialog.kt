package com.example.siado.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.siado.R
import com.example.siado.ui.CameraActivity
import com.example.siado.utils.BitmapRotator

class TrueDialog() {

    fun showDialog(
        name: String,
        status: Int,
        context: Context,
    ) {

        val dialog = Dialog(context)

        // set dialog properties
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_valid)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvWelcome: TextView = dialog.findViewById(R.id.tv_welcome)
        tvWelcome.text = when (status) {
            0, 2 -> "Welcome, "
            else -> "See you, "
        }

        val tvName: TextView = dialog.findViewById(R.id.tv_name)
        tvName.text = name

        val btnDone: Button = dialog.findViewById(R.id.btn_done)
        btnDone.setOnClickListener {
            Log.d("btnDialog", "clicked!")

            dialog.dismiss()
        }

        // show the dialog
        dialog.show()
    }
}