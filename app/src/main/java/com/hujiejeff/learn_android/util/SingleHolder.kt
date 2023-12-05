package com.hujiejeff.learn_android.util

import android.content.Context

//单例推荐
open class SingleHolder<out T, in A>(private val constructor: (A) -> T) {
    @Volatile
    private var instance: T? = null
    fun getInstance(arg: A): T = instance ?: synchronized(this) {
        instance ?: constructor(arg).also { instance = it }
    }
}

/**
 * val singleInstance = SampleSingleCall.getInstance(arg)
 */
class SampleSingleClass private constructor(context: Context) {
    fun doSomething() {

    }
    companion object: SingleHolder<SampleSingleClass, Context>(::SampleSingleClass)
}


