package com.hujiejeff.musicplayer.discover

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.base.AbstractLazyLoadFragment
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.*
import com.hujiejeff.musicplayer.databinding.HomeFragmentDiscoverBinding
import com.hujiejeff.musicplayer.databinding.ItemAlbumListBinding
import com.hujiejeff.musicplayer.databinding.ItemPlaylistListBinding
import com.hujiejeff.musicplayer.discover.playlist.PlaylistActivity
import com.hujiejeff.musicplayer.discover.playlistsqure.MainPlaylistActivity
import com.hujiejeff.musicplayer.discover.search.SearchActivity
import com.hujiejeff.musicplayer.util.*

class DiscoverFragment : AbstractLazyLoadFragment<HomeFragmentDiscoverBinding>() {

    private val recomendPlaylistList = mutableListOf<RecommendPlayList>()

    private val newSongList = mutableListOf<RecommendNewSong>()

    private val newAlbumList = mutableListOf<RecommendNewAlbum>()

    private lateinit var viewModel: DiscoverViewModel


    override fun HomeFragmentDiscoverBinding.initView() {

        rvRecomendPlaylists.apply {
            adapter =
                createAdapter<ItemPlaylistListBinding, RecommendPlayList>(recomendPlaylistList).apply {
                    setOnItemClickListener {
                        PlaylistActivity.start(
                            recomendPlaylistList[it].id,
                            recomendPlaylistList[it].picUrl,
                            requireActivity()
                        )
                    }
                }
            layoutManager = GridLayoutManager(context, 3)
            itemAnimator = DefaultItemAnimator()
        }

        rvNewSongs.apply {
            adapter = createAdapter<ItemAlbumListBinding, RecommendNewSong>(newSongList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = DefaultItemAnimator()
        }

        rvNewAlbums.apply {
            adapter = createAdapter<ItemAlbumListBinding, RecommendNewAlbum>(newAlbumList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = DefaultItemAnimator()
        }

        includeBinding.etSearch.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            context?.startActivity(intent)
        }

        bnvDiscoverTop.apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_discover_top_item_playlist -> {
                        (activity as AppCompatActivity).transaction {
                            context.startActivity(Intent(context, MainPlaylistActivity::class.java))
                            addToBackStack(null)
                        }
                    }
                }
                true
            }
        }
    }

    override fun getTAG(): String = "DiscoverFragment"

    override fun onLoadData() {
        viewModel.start()
    }

    override fun onPermissionFailed() {

    }

    override fun getPermissions(): Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = obtainViewModel()
        subscribe()
    }

    private fun subscribe() {
        logD("subscribe")

        viewModel.apply {
            recomendPlaylists.observe {
                logD("observe")
                recomendPlaylistList.addAll(it)
                mBinding.rvRecomendPlaylists.adapter?.notifyDataSetChanged()
            }
            newSongs.observe {
                newSongList.addAll(it)
                mBinding.rvNewSongs.adapter?.notifyDataSetChanged()
            }

            newAlbums.observe {
                newAlbumList.addAll(it)
                mBinding.rvNewAlbums.adapter?.notifyDataSetChanged()
            }
            loading.observe {
                var isLoading = false
                if (it == 0) {
                    isLoading = true
                } else if (it == 3) {
                    isLoading = false
                }
                mBinding.groupContent.visibility = if (isLoading) View.GONE else View.VISIBLE
                mBinding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun <T> LiveData<T>.observe(action: (T) -> Unit) {
        observe(this@DiscoverFragment, Observer { action(it) })
    }

    private fun obtainViewModel() = obtainViewModel(DiscoverViewModel::class.java)


    private inline fun <reified T : ViewBinding, D> createAdapter(datas: List<D>): BaseRecyclerViewAdapter<T, D> {
        return object : BaseRecyclerViewAdapter<T, D>(context, datas) {
            override fun convert(holder: BaseViewHolder<T>, data: D, position: Int) {
                when (data) {
                    is RecommendPlayList -> {
                        (holder.mBinding as ItemPlaylistListBinding).run {
                            tvPlaylistName.text = data.name
                            tvPlaylistPlayCount.text = (data.playCount / 10000).toString() + "万"
                            ivPlaylistCover.loadPlayListCover(data.picUrl)
                        }
                    }
                    is RecommendNewAlbum -> {
                        (holder.mBinding as ItemAlbumListBinding).run {
                            root.layoutParams.width = root.context.dp2Px(140)
                            albumCover.loadPlayListCover(data.picUrl)
                            albumTitle.text = data.name
                            albumArtist.text = data.artist.name
                        }
                    }
                    is RecommendNewSong -> {
                        (holder.mBinding as ItemAlbumListBinding).run {
                            root.layoutParams.width = root.context.dp2Px(140)
                            albumCover.loadPlayListCover(data.picUrl)
                            albumTitle.text = data.name
                            albumArtist.text = data.song.artists[0].name
                        }
                    }
                }
            }
        }
    }
}