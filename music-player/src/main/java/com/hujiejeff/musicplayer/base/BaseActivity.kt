package com.hujiejeff.musicplayer.base

import PermissionReq
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.customview.PlayerProgressView
import com.hujiejeff.musicplayer.data.Preference
import com.hujiejeff.musicplayer.player.MusicPlayFragment
import com.hujiejeff.musicplayer.util.dp2Px
import com.hujiejeff.musicplayer.util.getCover
import com.hujiejeff.musicplayer.util.setActivityContentView
import com.hujiejeff.musicplayer.util.transaction


abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {


    protected lateinit var mBinding: V
    private val musicPlayFragment by lazy { MusicPlayFragment() }
    private var isAddMusicPlay = false
    private var isShowMusicPlay = false

    protected abstract fun isLightStatusBar(): Boolean
    protected open var mStatusBarColorId: Int = android.R.color.transparent
    protected open var mHasStatusBar: Boolean = true
    protected open fun getToolbar(): Toolbar? = null
    private lateinit var ppv: PlayerProgressView
    protected abstract fun V.initView()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setActivityContentView(layoutInflater)!!
        mBinding.initView()
        setStatusBar()
        setSupportActionBar(getToolbar())
        addPlayBar()
        subscribe()
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BarUtils.setStatusBarColor(this, resources.getColor(mStatusBarColorId, theme), true)
        } else {
            BarUtils.setStatusBarColor(this, resources.getColor(mStatusBarColorId), true)
        }
        BarUtils.setStatusBarLightMode(this, isLightStatusBar())
        if (mHasStatusBar) {
            BarUtils.addMarginTopEqualStatusBarHeight(mBinding.root)
        }
    }

    private fun addPlayBar() {

        val rootView = (findViewById<ViewGroup>(android.R.id.content))
        if (rootView is FrameLayout) {
            val lp: FrameLayout.LayoutParams =
                FrameLayout.LayoutParams(dp2Px(70), dp2Px(70)).apply {
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
            currentMusic.observe(this@BaseActivity) { music ->
                ppv.max = music.duration.toInt()
                ppv.progress = if (!isPlay.value!!) Preference.play_progress else 0

                if (music.type == 1) {
                    ppv.setSrc(music.coverSrc!!)
                } else {
                    ppv.setBitmap(getCover(music.albumID))
                }
            }

            playProgress.observe(this@BaseActivity) { progress ->
                ppv.progress = progress
            }

            isPlayFragmentShow.observe(this@BaseActivity) { isShow ->
                if (isShow) {
                    openMusicPlayFragment()
                } else {
                    hideMusicPlayFragment()
                }
            }
        }
    }

    private fun openMusicPlayFragment() {
        ViewCompat.setTransitionName(ppv, "MusicPlayFragment")
        transaction {
            if (!isAddMusicPlay) {
                add(android.R.id.content, musicPlayFragment)
                isAddMusicPlay = true
            }
//            addSharedElement(ppv, "MusicPlayFragment")
//            musicPlayFragment.sharedElementEnterTransition = Explode()
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
