package com.hujiejeff.musicplayer.discover.playlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.base.App
import com.hujiejeff.musicplayer.base.BaseActivity
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.PlayListDetail
import com.hujiejeff.musicplayer.data.entity.Track
import com.hujiejeff.musicplayer.databinding.ActivityPlaylistBinding
import com.hujiejeff.musicplayer.databinding.ItemSearchResultSongBinding
import com.hujiejeff.musicplayer.util.obtainViewModel

/**
 * Create by hujie on 2020/3/13
 * 歌单详情
 */
class PlaylistActivity : BaseActivity() {
    companion object {
        const val EXTRA_PLAYLIST_ID = "extra_playlist_id"
        const val EXTRA_PLAYLIST_COVER_URL = "extra_playlist_cover_url"
        fun start(id: Long, coverUrl: String, activity: Activity) {
            val intent = Intent(activity, PlaylistActivity::class.java)
            intent.putExtra(EXTRA_PLAYLIST_ID, id)
            intent.putExtra(EXTRA_PLAYLIST_COVER_URL, coverUrl)
            activity.startActivity(intent)
        }
    }

    private var isLoadedList = false
    private val trackList: MutableList<Track> = mutableListOf()

    private lateinit var playListDetail: PlayListDetail

    private lateinit var viewModel: PlaylistViewModel

    private var id: Long = 0

    private lateinit var binding: ActivityPlaylistBinding
    private lateinit var coverUrl: String
    override fun layoutResId(): Int = R.layout.activity_playlist
    override fun isLightStatusBar(): Boolean = false
    override fun getViewBinding(): View {
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getToolbar(): Toolbar = binding.toolbar

    private fun obtainViewModel(): PlaylistViewModel =
        obtainViewModel(PlaylistViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getLongExtra(EXTRA_PLAYLIST_ID, 0)
        coverUrl = intent.getStringExtra(EXTRA_PLAYLIST_COVER_URL)!!
        binding.initView()
        viewModel = obtainViewModel()
        subscribe()
        viewModel.loadPlaylistDetail(id)
    }

    private fun ActivityPlaylistBinding.initView() {
//        iv_playlist_detail_cover.loadPlayListCover(coverUrl)

        rvPlaylistMusicList.apply {
            adapter = Adapter().apply {
                setOnItemClickListener { position ->
                    if (!isLoadedList) {
                        App.playerViewModel.loadNetMusicList(trackList)
                        isLoadedList = true
                    }
                    App.playerViewModel.play(position)
                }
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }


    }


    private fun subscribe() {
        viewModel.apply {
            loading.observe(this@PlaylistActivity) { isLoading ->
                binding.rvPlaylistMusicList.visibility =
                    if (isLoading) View.INVISIBLE else View.VISIBLE
                if (isLoading) {
                    binding.loadingView.show()
                } else {
                    binding.loadingView.hide()
                }
            }

            playlistDetail.observe(this@PlaylistActivity) {
                trackList.addAll(it.tracks)
                binding.rvPlaylistMusicList.adapter?.notifyDataSetChanged()
                playListDetail = it
                updateUI()
            }
        }


    }

    private fun updateUI() {
        /*tv_playlist_detail_name.text = playListDetail.name
        tv_playlist_detail_desc.text = playListDetail.description.substring(0, 20)
        tv_playlist_detail_comment_count.text = playListDetail.commentCount.toString()
        tv_playlist_detail_share_count.text = playListDetail.shareCount.toString()*/
    }

    inner class Adapter :
        BaseRecyclerViewAdapter<ItemSearchResultSongBinding, Track>(this, trackList) {
        override fun convert(
            holder: BaseViewHolder<ItemSearchResultSongBinding>,
            data: Track,
            position: Int
        ) {
            holder.mBinding.run {
                tvItemSongName.text = data.name
                tvItemArtistName.text = data.ar[0].name
            }
        }
    }

}