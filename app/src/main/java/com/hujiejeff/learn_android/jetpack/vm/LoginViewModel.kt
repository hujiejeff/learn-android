package com.hujiejeff.learn_android.jetpack.vm

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class LoginViewModel : ViewModel() {
    val userName: MutableLiveData<String> by lazy {
        MutableLiveData("ff")
    }

    val ss = userName.asFlow()

    val page = MutableStateFlow<Int>(1)
    private val api: Flow<List<String>> = page.transform {
        emit(getNewsList(it))
    }

    val newsList2: StateFlow<List<String>> = page.map {
        getNewsList(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed(5_000)
    )

    val newsList: StateFlow<List<String>> = api.stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed(5_000),
    )

    val newsList3: StateFlow<LoginActivityUiState> = page.transform {
        emit(LoginActivityUiState.DataLoading)
        emit(LoginActivityUiState.Success(getNewsList(it)))
    }.stateIn(
        scope = viewModelScope,
        initialValue = LoginActivityUiState.FirstLoading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private suspend fun getNewsList(page: Int): List<String> {
        delay(2000)
        val list = mutableListOf<String>()
        repeat(page) {
            list.add("$page$page")
        }
        return list
    }

    fun loadNextPage() {
        page.update {
            it + 1
        }
    }

    sealed class LoginActivityUiState {
        object FirstLoading : LoginActivityUiState()
        data class Success(val data: List<String>) : LoginActivityUiState()
        object DataLoading : LoginActivityUiState()
    }
}