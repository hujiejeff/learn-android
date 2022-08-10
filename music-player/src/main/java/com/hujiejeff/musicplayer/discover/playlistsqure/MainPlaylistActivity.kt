package com.hujiejeff.musicplayer.discover.playlistsqure

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.base.BaseActivity
import com.hujiejeff.musicplayer.databinding.ActivityMainPlaylistBinding
import com.hujiejeff.musicplayer.util.obtainViewModel

/**
 * Create by hujie on 2020/3/12
 */
class MainPlaylistActivity: BaseActivity() {
    private val fragmentList: MutableList<Fragment> = mutableListOf()

    private lateinit var viewModel: MainPlaylistViewModel
    private lateinit var binding: ActivityMainPlaylistBinding
    override fun layoutResId(): Int = R.layout.activity_main_playlist
    override fun isLightStatusBar(): Boolean = true
    override fun getViewBinding(): View {
        binding = ActivityMainPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getToolbar(): Toolbar = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel()
        binding.vpMainPlaylistContainer.apply {
            adapter = PagerAdapter()
            binding.tbMainPlaylistCats.setupWithViewPager(this)
        }
        viewModel.loadSubCat()
        subscribe()
    }

    private fun subscribe() {
        viewModel.subCatList.observe(this) { list ->
            list.forEach {
                fragmentList.add(PlaylistListFragment().apply { subCat = it })
            }
            binding.vpMainPlaylistContainer.adapter?.notifyDataSetChanged()
        }
    }



    fun obtainViewModel(): MainPlaylistViewModel = obtainViewModel(
        MainPlaylistViewModel::class.java)

    inner class PagerAdapter :
        FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragmentList[position]

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence? = viewModel.subCatList.value?.get(position)?.name

    }
}