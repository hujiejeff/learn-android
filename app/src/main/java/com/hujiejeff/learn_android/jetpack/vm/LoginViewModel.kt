package com.hujiejeff.learn_android.jetpack.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel:ViewModel() {
    val userName: MutableLiveData<String> by lazy {
        MutableLiveData("ff")
    }
}