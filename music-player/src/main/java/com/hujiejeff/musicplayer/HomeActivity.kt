package com.hujiejeff.musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.view.forEach
import com.google.android.material.badge.BadgeDrawable
import com.hujiejeff.musicplayer.base.BaseActivity
import com.hujiejeff.musicplayer.databinding.ActivityHomeBinding
import com.hujiejeff.musicplayer.discover.DiscoverFragment
import com.hujiejeff.musicplayer.localmusic.LocalMusicFragment
import com.hujiejeff.musicplayer.service.PlayService
import com.hujiejeff.musicplayer.util.transaction
import com.hujiejeff.musicplayer.video.VideoFragment


class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun isLightStatusBar(): Boolean = true
    private val fragments = arrayListOf(
        DiscoverFragment(),
        LocalMusicFragment(), VideoFragment()
    )
    private var preIndex: Int = 0

    override fun ActivityHomeBinding.initView() {
        loadFragment(0)
        bindService()
        initClickListener()
    }


    private fun bindService() {
        val bindIntent = Intent(this, PlayService::class.java)
        val serviceConnection = object : ServiceConnection {
            override fun onServiceDisconnected(p0: ComponentName?) {
            }

            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            }
        }
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }


    private fun initClickListener() {
        mBinding.bnvBottom.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bottom_item_discover -> showAndHidFragment(0, preIndex)
                R.id.menu_bottom_item_my -> showAndHidFragment(1, preIndex)
                R.id.menu_bottom_item_video -> showAndHidFragment(2, preIndex)
            }
            true
        }
        setUpBadge()
    }

    private fun setUpBadge() {
        mBinding.bnvBottom.run {
            menu.forEach {
                getOrCreateBadge(it.itemId).run {
                    isVisible = true
                    badgeGravity = BadgeDrawable.TOP_END//定位
                    badgeTextColor = resources.getColor(R.color.white)//颜色
                    backgroundColor = resources.getColor(R.color.colorAccent)//背景色
                    if (it == menu.getItem(2)) {
                        number = 999//数目
                    }
                }
            }
        }
    }


    private fun loadFragment(showIndex: Int) {
        transaction {
            for (i in fragments.indices) {
                add(R.id.fragment_container, fragments[i], fragments[i].tag)
                if (i != showIndex) {
                    hide(fragments[i])
                }
            }
            preIndex = showIndex
        }
    }

    private fun showAndHidFragment(showIndex: Int, hideIndex: Int) {
        if (showIndex != hideIndex) {
            transaction {
                supportFragmentManager.popBackStackImmediate()
                hide(fragments[hideIndex])
                show(fragments[showIndex])
            }
            preIndex = showIndex
        }
    }
}