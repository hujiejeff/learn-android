package com.hujiejeff.learn_android.jetpack.vm

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class LoginViewModel : ViewModel() {
    val userName: MutableLiveData<String> by lazy {
        MutableLiveData("ff")
    }

    val ss = userName.asFlow()

    private val page = MutableStateFlow<Int>(1)
    private val api: Flow<List<String>> = page.transform {
        emit(getNewsList(it))
    }

    val newsList: StateFlow<List<String>> = api.stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
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
}