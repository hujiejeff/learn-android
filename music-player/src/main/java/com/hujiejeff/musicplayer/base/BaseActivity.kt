package com.hujiejeff.musicplayer.base

import PermissionReq
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.customview.PlayerProgressView
import com.hujiejeff.musicplayer.data.Preference
import com.hujiejeff.musicplayer.data.entity.Music
import com.hujiejeff.musicplayer.player.MusicPlayFragment
import com.hujiejeff.musicplayer.util.*


abstract class BaseActivity: AppCompatActivity() {


    private val musicPlayFragment by lazy { MusicPlayFragment() }
    private var isAddMusicPlay = false
    private var isShowMusicPlay = false


    protected abstract fun layoutResId(): Int
    protected abstract fun isLightStatusBar(): Boolean
    protected abstract fun getViewBinding(): View
    open protected fun getToolbar(): Toolbar? = null
    private lateinit var ppv: PlayerProgressView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getViewBinding())
        checkAndroidVersionAction(Build.VERSION_CODES.M, {
            window.setTransparentStatusBar(isLightStatusBar())
        })
        setSupportActionBar(getToolbar())
        addPlayBar()
        subscribe()
    }

    private fun addPlayBar() {

        val rootView = (findViewById<ViewGroup>(android.R.id.content))
        if (rootView is FrameLayout) {
            val lp: FrameLayout.LayoutParams = FrameLayout.LayoutParams(dp2Px(70), dp2Px(70)).apply {
                gravity = Gravity.BOTTOM or Gravity.END
                rightMargin = dp2Px(10)
                bottomMargin = dp2Px(100)
            }
            ppv = PlayerProgressView(this).apply {
                id = R.id.ppv_player
                layoutParams = lp
            }
            rootView.addView(ppv)
        }

        ppv.setOnClickListener {
            App.playerViewModel.showOrHidePlayFragment()
        }
    }

    private fun subscribe() {
        App.playerViewModel.apply {
            currentMusic.observe(this@BaseActivity,
                Observer<Music> { music ->
                    ppv.max = music.duration.toInt()
                    ppv.progress = if (!isPlay.value!!) Preference.play_progress else 0

                    if (music.type == 1) {
                        ppv.setSrc(music.coverSrc!!)
                    } else {
                        ppv.setBitmap(getCover(music.albumID))
                    }
                })

            playProgress.observe(this@BaseActivity, Observer<Int> { progress ->
                ppv.progress = progress
            })

            isPlayFragmentShow.observe(this@BaseActivity, Observer {isShow ->
                if (isShow) {
                    checkAndroidVersionAction(Build.VERSION_CODES.M, {
                        window.setTransparentStatusBar(true)
                    })
                    openMusicPlayFragment()
                } else {
                    checkAndroidVersionAction(Build.VERSION_CODES.M, {
                        window.setTransparentStatusBar(isLightStatusBar())
                    })
                    hideMusicPlayFragment()
                }
            })
        }
    }

    private fun openMusicPlayFragment() {
        transaction {
            if (!isAddMusicPlay) {
                add(android.R.id.content, musicPlayFragment)
                isAddMusicPlay = true
            }
            setCustomAnimations(R.anim.fragment_slide_up, 0)
            show(musicPlayFragment)
            isShowMusicPlay = true
        }
    }

    private fun hideMusicPlayFragment() {
        transaction {
            setCustomAnimations(0, R.anim.fragment_slide_down)
            hide(musicPlayFragment)
            isShowMusicPlay = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionReq.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        if (isShowMusicPlay) {
            App.playerViewModel.showOrHidePlayFragment()
            return
        }
        super.onBackPressed()
    }
}
