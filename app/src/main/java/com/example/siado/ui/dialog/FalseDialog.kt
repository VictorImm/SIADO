package com.example.siado.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.siado.R
import com.example.siado.ui.camera.present.CameraActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

            // reset dialog status
            CameraActivity.dialogStatusLiveData.postValue(0)

            dialog.dismiss()
        }

        // show the dialog
        dialog.show()

        // close dialog when 10s idle
        CoroutineScope(Dispatchers.Main).launch {
            delay(10000)

            if (CameraActivity.dialogStatusLiveData.value == 1) {
                CameraActivity.dialogStatusLiveData.postValue(0)
                dialog.dismiss()
            }
        }
    }
}