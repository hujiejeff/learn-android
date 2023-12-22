package com.hujiejeff.learn_android.compose.material

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MaterialDemoViewModel(savedStateHandle: SavedStateHandle):ViewModel() {
    val title = checkNotNull(savedStateHandle["title"])
    val arg: String? = savedStateHandle["arg"]
}