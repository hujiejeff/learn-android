package com.hujiejeff.musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import com.hujiejeff.musicplayer.base.BaseActivity
import com.hujiejeff.musicplayer.databinding.ActivityHomeBinding
import com.hujiejeff.musicplayer.discover.DiscoverFragment
import com.hujiejeff.musicplayer.localmusic.LocalMusicFragment
import com.hujiejeff.musicplayer.service.PlayService
import com.hujiejeff.musicplayer.util.transaction
import com.hujiejeff.musicplayer.video.VideoFragment


class HomeActivity : BaseActivity() {
    override fun layoutResId(): Int = R.layout.activity_home
    override fun isLightStatusBar(): Boolean = true
    private lateinit var binding: ActivityHomeBinding
    override fun getViewBinding(): View {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    private val fragments = arrayListOf(
        DiscoverFragment(),
        LocalMusicFragment(), VideoFragment()
    )
    private var preIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        bindService()
        initClickListener()
    }

    private fun initView() {
        loadFragment(0)
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
        binding.bnvBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bottom_item_discover -> showAndHidFragment(0, preIndex)
                R.id.menu_bottom_item_my -> showAndHidFragment(1, preIndex)
                R.id.menu_bottom_item_video -> showAndHidFragment(2, preIndex)
            }
            true
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