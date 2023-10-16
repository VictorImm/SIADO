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
import com.example.siado.R
import com.example.siado.ui.CameraActivity
import com.example.siado.utils.BitmapRotator

class FalseDialog {

    fun showDialog(
        errorCode: Int,
        context: Context,
    ) {

        val dialog = Dialog(context)

        // set dialog properties
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_invalid)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvMessage: TextView = dialog.findViewById(R.id.tv_error)
        tvMessage.text = when (errorCode) {
            3 -> "Already present!"
            else -> "Invalid time arrival!"

        }

        val btnClose: Button = dialog.findViewById(R.id.btn_close)
        btnClose.setOnClickListener {
            Log.d("btnDialog", "clicked!")

            dialog.dismiss()
        }

        // show the dialog
        dialog.show()
    }
}