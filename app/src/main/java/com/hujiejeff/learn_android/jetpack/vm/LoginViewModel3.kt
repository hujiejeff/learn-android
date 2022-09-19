package com.hujiejeff.learn_android.jetpack.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LoginViewModel3(val savedStateHandle: SavedStateHandle): ViewModel() {
    fun hello() {
        Log.d("LoginViewModel3" , savedStateHandle.toString())
    }
}