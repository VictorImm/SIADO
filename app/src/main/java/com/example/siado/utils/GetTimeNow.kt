package com.example.siado.utils

import com.example.siado.data.DateTime
import java.time.LocalDateTime

object GetTimeNow {

    fun getDateToday(): DateTime {
        val current = LocalDateTime.now()

        return DateTime(
            date = current.dayOfMonth,
            mon = current.monthValue,
            year = current.year,
            hour = current.hour,
            min = current.minute,
            sec = current.second
        )
    }

}