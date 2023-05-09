package com.hujiejeff.learn_android.base

import android.app.Application

class CommonApplication: Application() {
    companion object {
        private lateinit var application: Application
        fun get() = application
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }


}