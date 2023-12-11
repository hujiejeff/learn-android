package com.hujiejeff.learn_android.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ComposeDemoViewModel : ViewModel() {
    private val intentChannel: Channel<Intent> = Channel(Channel.UNLIMITED)
    val activityIntentFlow = intentChannel.receiveAsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Intent.Empty)

    fun composeNavi(route: Route) {
        viewModelScope.launch {
            intentChannel.send(Intent.NavigationIntent(route.route))
        }
    }

    sealed class Intent {
        class NavigationIntent(val route: String) : Intent()

        object Empty : Intent()
    }
}