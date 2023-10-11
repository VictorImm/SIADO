package com.example.siado.data.application

import android.app.Application
import com.example.siado.data.user.UserRoomDatabase

class Application: Application() {
    val database: UserRoomDatabase by lazy {
        UserRoomDatabase.getDatabase(applicationContext)
    }
}