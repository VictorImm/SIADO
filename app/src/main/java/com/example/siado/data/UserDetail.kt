package com.example.siado.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
class UserDetail(
    val uid: String,
    val img: String,
    val name: String,
    val status: Int,
    val arrival: Int,
    val date: Int,
    val mon: Int,
    val year: Int,
    val hour: Int,
    val min: Int,
    val sec: Int
): Parcelable {
    constructor() : this("", "", "", -1, -1, -1, -1, -1, -1, -1, -1)
}