package com.example.siado.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.siado.data.DateTime
import com.example.siado.data.UserDetail
import com.example.siado.data.user.User
import com.example.siado.data.user.UserDao
import com.example.siado.ui.CameraActivity
import com.example.siado.ui.MainActivity.Companion.databaseUrl
import com.example.siado.ui.dialog.FalseDialog
import com.example.siado.ui.dialog.TrueDialog
import com.example.siado.utils.BitmapRotator
import com.example.siado.utils.PresentCallback
import com.example.siado.utils.PrimaryKeyMaker
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

class UserViewModel(private val userDao: UserDao): ViewModel() {

    // insert item
    private fun addNewUser(
        image: Bitmap,
        name: String,
        dateTime: DateTime,
        status: Int,
        arrival: Int
    ) {
        val newUser = User(
            id = PrimaryKeyMaker.make(dateTime),
            name = name,
            status = status,
            arrival = arrival,
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
        addNewUserToFirebase(image, newUser)
    }

    private fun addNewUserToFirebase(image: Bitmap, user: User) {

        val fileName = UUID.randomUUID().toString()

        val dbRef = FirebaseDatabase.getInstance(databaseUrl).getReference("/user").push()
        val fsRef = FirebaseStorage.getInstance().getReference("/images/$fileName")

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        fsRef.putBytes(data)
            .addOnSuccessListener {
                fsRef.downloadUrl
                    .addOnSuccessListener {  photoUri ->
                        val newUser = UserDetail(
                            uid = user.id,
                            img = photoUri.toString(),
                            name = user.name,
                            status = user.status,
                            arrival = user.arrival,
                            date = user.date,
                            mon = user.mon,
                            year = user.year,
                            hour = user.hour,
                            min = user.min,
                            sec = user.sec
                        )
                        dbRef.setValue(newUser)
                    }

            }
            .addOnFailureListener {
                // TODO: add failure listener
            }
    }

    fun present(
        image: Bitmap,
        name: String,
        dateTime: DateTime,
        context: Context
    ) {
        CameraActivity.dialogStatusLiveData.postValue(1)

        val image = BitmapRotator.rotate(image)

        val presentCallbackProperties = PresentCallbackProperties(userDao)
        presentCallbackProperties.setPresentCallback(object : PresentCallback {
            override fun onSuccessPresent(result: Int) {
                val trueDialog = TrueDialog()
                val falseDialog = FalseDialog()

                Log.d("callbackResult", "$result")

                when (result) {
                    0, 1, 2 -> { // 0 = entry, 1 = depart, 2 = late
                        // save user to database
                        addNewUser(
                            image,
                            name,
                            dateTime,
                            when (result) {
                                2 -> 0
                                else -> result
                            },
                            when (result) {
                                0, 1 -> 1
                                else -> 0
                            }
                        ) // valid

                        trueDialog.showDialog(
                            name,
                            result,
                            context,
                        )
                    }
                    else -> {
                        falseDialog.showDialog(
                            result,
                            context
                        )
                    }
                }
            }

            override fun onErrorPresent(error: Throwable) {
                Log.e("presentCallback", "on error: ${error.message}", error)
            }
        })

        viewModelScope.launch {
            presentCallbackProperties.saveUserAndReturnStatus(name, dateTime)
        }
    }

    fun clear() {
        viewModelScope.launch {
            userDao.clear()
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

// callback property
class PresentCallbackProperties(private val userDao: UserDao) {

    private var presentCallback: PresentCallback? = null

    fun setPresentCallback(callback: PresentCallback) {
        presentCallback = callback
    }

    suspend fun saveUserAndReturnStatus(
        name: String,
        dateTime: DateTime
    ) {
        try {
            val result = when (dateTime.hour) {
                // entry time
                in 6..11 -> {
                    // check if user is already present (prevent redundant data)
                    if (userDao.isExist(name, 0) == 1) {
                        // already present
                        3
                    } else {
                        // valid, entry
                        0
                    }
                }

                // out time
                in 12 .. 21 -> {
                    // check if user present in entry time
                    if (userDao.isExist(name, 0) == 1) {
                        // if present, means valid
                        // check if user is already present (prevent redundant data)
                        if (userDao.isExist(name, 1) == 1) {
                            // already present
                            3
                        } else {
                            // valid, depart
                            1
                        }
                    } else {
                        // if not present yet, means late

                        // TODO: harus diapakan orang yang terlambat?
                        // late
                        2
                    }
                }

                // invalid hour
                else -> {
                    // invalid hour
                    4
                }
            }

            presentCallback?.onSuccessPresent(result)
        } catch (error: Throwable) {
            presentCallback?.onErrorPresent(error)
        }
    }
}