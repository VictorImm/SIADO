package com.example.siado.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserScanned (
    val name: String,
    val prob: Float,
): Parcelable