package com.example.siado.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateTime(
    val date: Int,
    val mon: Int,
    val year: Int,
    val hour: Int,
    val min: Int,
    val sec: Int
): Parcelable {
    constructor(parcel: Parcel): this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {

    }

    companion object : Parceler<DateTime> {

        override fun DateTime.write(parcel: Parcel, flags: Int) {
            parcel.writeValue(date)
            parcel.writeValue(mon)
            parcel.writeValue(year)
            parcel.writeValue(hour)
            parcel.writeValue(min)
            parcel.writeValue(sec)
        }

        override fun create(parcel: Parcel): DateTime {
            return DateTime(parcel)
        }
    }
}
