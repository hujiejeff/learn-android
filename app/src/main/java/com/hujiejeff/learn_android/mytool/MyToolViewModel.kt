package com.hujiejeff.learn_android.mytool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MyToolViewModel: ViewModel() {
    val qrScanResult =  MutableStateFlow<String>("")

    private val intentChannel: Channel<Intent> = Channel(Channel.UNLIMITED)

    val intentFlow = intentChannel.receiveAsFlow()


    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    sealed class Intent {
        class ScanByCamera : Intent() {
            override fun equals(other: Any?): Boolean {
                return this === other
            }

            override fun hashCode(): Int {
                return System.identityHashCode(this)
            }
        }

        class ScanByAlbum : Intent() {
            override fun equals(other: Any?): Boolean {
                return this === other
            }

            override fun hashCode(): Int {
                return System.identityHashCode(this)
            }
        }
    }
}