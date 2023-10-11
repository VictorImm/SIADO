package com.example.siado.utils

import com.example.siado.data.DateTime

object PrimaryKeyMaker {

    fun make(dateTime: DateTime): Int {
        val key = "${dateTime.date}${dateTime.mon}${dateTime.year}${dateTime.hour}${dateTime.min}${dateTime.sec}"
        return (key.toInt())
    }

}