package com.example.siado.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.siado.data.DateTime
import com.example.siado.data.user.User
import com.example.siado.data.user.UserDao
import com.example.siado.utils.PrimaryKeyMaker
import kotlinx.coroutines.launch
import kotlin.math.min

class UserViewModel(private val userDao: UserDao): ViewModel() {

    // insert item
    private fun addNewUser(
        name: String,
        job: String,
        dateTime: DateTime
    ) {
        val newUser = User(
            id = PrimaryKeyMaker.make(dateTime),
            name = name,
            job = job,
            date = dateTime.date,
            mon = dateTime.mon,
            year = dateTime.year,
            hour = dateTime.hour,
            min = dateTime.min,
            sec = dateTime.sec
        )
        viewModelScope.launch {
            userDao.insert(newUser)
        }

        // TODO: gateway to firebase database
    }

    fun present(
        name: String,
        job: String,
        dateTime: DateTime
    ) {

        when (dateTime.hour) {
            // entry time
            in 6..11 -> {
                addNewUser(name, job, dateTime)
            }

            // out time
            in 12 .. 19 -> {
                // check if user is present this morning
                viewModelScope.launch{
                    if (userDao.isExist(name) == 1) {
                        // if present, means valid
                        addNewUser(name, job, dateTime)
                    } else {
                        // if not present yet, means late
                        // TODO: harus diapakan orang yang terlambat?
                    }
                }
            }

            // invalid hour
            else -> {
                // TODO: create invalid hour dialog
            }
        }
    }

}

class UserViewModelFactory(private val userDao: UserDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}