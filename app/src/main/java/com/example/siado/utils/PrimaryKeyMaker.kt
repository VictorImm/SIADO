package com.example.siado.utils

import com.example.siado.data.DateTime

object PrimaryKeyMaker {

    fun make(dateTime: DateTime): String {
        return "${dateTime.date}${dateTime.mon}${dateTime.year}${dateTime.hour}${dateTime.min}${dateTime.sec}"
    }

}