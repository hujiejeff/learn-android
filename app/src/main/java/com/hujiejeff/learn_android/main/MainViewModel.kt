package com.hujiejeff.learn_android.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
     var isReady = false
    init {

    }
    fun initLoading() {
        viewModelScope.launch {
            delay(2000)
            isReady = true
        }
    }
}