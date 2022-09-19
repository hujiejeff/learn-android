package com.hujiejeff.learn_android.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle

class MainViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, savedStateHandle: SavedStateHandle) : this(application) {
    }

}