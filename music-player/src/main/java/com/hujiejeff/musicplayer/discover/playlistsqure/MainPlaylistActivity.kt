package com.hujiejeff.musicplayer.discover.playlistsqure

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.hujiejeff.musicplayer.base.BaseActivity
import com.hujiejeff.musicplayer.databinding.ActivityMainPlaylistBinding
import com.hujiejeff.musicplayer.util.obtainViewModel

/**
 * Create by hujie on 2020/3/12
 */
class MainPlaylistActivity : BaseActivity<ActivityMainPlaylistBinding>() {
    private val fragmentList: MutableList<Fragment> = mutableListOf()

    private lateinit var viewModel: MainPlaylistViewModel

    override fun isLightStatusBar(): Boolean = true

    override fun getToolbar(): Toolbar = mBinding.toolbar


    override fun ActivityMainPlaylistBinding.initView() {
        viewModel = obtainViewModel()
        vpMainPlaylistContainer.apply {
            adapter = PagerAdapter()
            tbMainPlaylistCats.setupWithViewPager(this)
        }
        viewModel.loadSubCat()
        subscribe()
    }

    private fun subscribe() {
        viewModel.subCatList.observe(this) { list ->
            list.forEach {
                fragmentList.add(PlaylistListFragment().apply { subCat = it })
            }
            mBinding.vpMainPlaylistContainer.adapter?.notifyDataSetChanged()
        }
    }


    fun obtainViewModel(): MainPlaylistViewModel = obtainViewModel(
        MainPlaylistViewModel::class.java
    )

    inner class PagerAdapter :
        FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragmentList[position]

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence? =
            viewModel.subCatList.value?.get(position)?.name

    }
}