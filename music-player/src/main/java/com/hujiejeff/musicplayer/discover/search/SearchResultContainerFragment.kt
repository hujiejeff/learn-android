package com.hujiejeff.musicplayer.discover.search

import android.Manifest
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.hujiejeff.musicplayer.base.AbstractLazyLoadFragment
import com.hujiejeff.musicplayer.data.entity.*
import com.hujiejeff.musicplayer.databinding.FragmentSearchResultContainerBinding
import com.hujiejeff.musicplayer.databinding.ItemSearchResultAlbumBinding
import com.hujiejeff.musicplayer.databinding.ItemSearchResultArtistBinding
import com.hujiejeff.musicplayer.databinding.ItemSearchResultSongBinding

/**
 * Create by hujie on 2020/3/6
 * viewpager搜索结果
 */
class SearchResultContainerFragment :
    AbstractLazyLoadFragment<FragmentSearchResultContainerBinding>() {
    private val fragmentList: List<Fragment> = listOf(
        SearchResultFragment.newInstance<ItemSearchResultSongBinding, SearchSong>(SearchType.SONG),
        SearchResultFragment.newInstance<ItemSearchResultArtistBinding, SearchArtist>(SearchType.ARTIST),
        SearchResultFragment.newInstance<ItemSearchResultAlbumBinding, SearchAlbum>(SearchType.ALBUM),
        SearchResultFragment.newInstance<ItemSearchResultAlbumBinding, SearchPlayList>(SearchType.PLAYLIST),
        SearchResultFragment.newInstance<ItemSearchResultArtistBinding, SearchUser>(SearchType.USER)
    )
    private val typeList: List<String> = listOf("单曲", "歌手", "专辑", "歌单", "用户")
    private lateinit var viewModel: SearchViewModel
    override fun getTAG(): String = "SearchResultContainerFragment"

    override fun FragmentSearchResultContainerBinding.initView() {
        vpSearchResult.adapter = PagerAdapter()
        tbSearchResult.setupWithViewPager(mBinding.vpSearchResult)

    }

    override fun onLoadData() {
        viewModel = (activity as SearchActivity).obtainViewModel()

    }


    override fun onPermissionFailed() {

    }

    override fun getPermissions(): Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )


    inner class PagerAdapter :
        FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragmentList[position]

        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence? = typeList[position]

    }
}