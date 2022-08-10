package com.hujiejeff.musicplayer.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.hujiejeff.musicplayer.ViewModelFactory
import com.hujiejeff.musicplayer.data.Preference
import com.hujiejeff.musicplayer.player.AudioPlayer
import com.hujiejeff.musicplayer.player.PlayerViewModel
import com.hujiejeff.musicplayer.util.logD

class App : Application(), ViewModelStoreOwner {
    private val viewModelProvider by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(this))
    }


    override fun getViewModelStore(): ViewModelStore = ViewModelStore()

    companion object {
        lateinit var appContext: Context
            private set

        lateinit var playerViewModel: PlayerViewModel
            private set

    }

    override fun onCreate() {
        super.onCreate()
        logD("App onCreate")
        appContext = applicationContext
        AudioPlayer.INSTANCE.init(appContext)
        Preference.init(this)

        playerViewModel = obtainViewModel(PlayerViewModel::class.java)
        playerViewModel.start()
    }

    fun todo() {
        //TODO 封面加载缓存问题 使用Glide加载，后续尝试实现自己实现缓存
        //TODO 懒加载 ok
        //TODO 状态栏 ok
        //TODO 播放页面viewpager ok
        //TODO 音频焦点 ok
        //TODO 耳机事件 ok
        //TODO 权限问题 ok
        //TODO 线程池集成 ok
        //TODO 当没有音乐的时候 ok
        //TODO MVVM重构

        //TODO 1、BUG 数组越界判断，无歌曲，错误的下标
        //TODO 2、
    }


    private fun <T : ViewModel> obtainViewModel(clazz: Class<T>): T {
        return viewModelProvider.get(clazz)
    }


}