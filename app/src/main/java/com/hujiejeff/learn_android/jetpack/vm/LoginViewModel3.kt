package com.hujiejeff.learn_android.jetpack.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LoginViewModel3(val savedStateHandle: SavedStateHandle): ViewModel() {
    val key = "test_key"
    init {
        savedStateHandle[key] = "init"
    }
    fun hello() {
        Log.d("LoginViewModel3" , savedStateHandle.toString())
        Log.d("LoginViewModel3" , savedStateHandle[key] ?: "empty")
        savedStateHandle[key] = "hello"
        Log.d("LoginViewModel3" , savedStateHandle[key] ?: "empty")
    }
}