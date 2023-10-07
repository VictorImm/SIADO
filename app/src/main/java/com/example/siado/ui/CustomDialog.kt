package com.example.siado.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.example.siado.R
import com.example.siado.utils.BitmapRotator

class CustomDialog() {

    fun showDialog(
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_captured)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnDone: Button = dialog.findViewById(R.id.btn_done)
        btnDone.setOnClickListener {
            Log.d("btnDialog", "clicked!")

            dialog.dismiss()

            (context as Activity).finish()
        }

        val ivCaptured: ImageView = dialog.findViewById(R.id.iv_captured)
        CameraActivity.bitmapLiveData.observe(lifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                ivCaptured.setImageBitmap(BitmapRotator.rotate(it))

                dialog.show()
            }
        })

    }

}