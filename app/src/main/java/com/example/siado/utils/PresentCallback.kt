package com.example.siado.utils

interface PresentCallback {
    fun onSuccessPresent(result: Int)
    fun onErrorPresent(error: Throwable)
}