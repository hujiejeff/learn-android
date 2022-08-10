package com.hujiejeff.musicplayer.localmusic.sub

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.hujiejeff.musicplayer.HomeActivity
import com.hujiejeff.musicplayer.base.AbstractLazyLoadFragment
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.Album
import com.hujiejeff.musicplayer.databinding.FragmentListBinding
import com.hujiejeff.musicplayer.databinding.ItemAlbumListBinding
import com.hujiejeff.musicplayer.localmusic.LocalMusicFragment
import com.hujiejeff.musicplayer.localmusic.LocalMusicViewModel
import com.hujiejeff.musicplayer.util.loadCover

class AlbumListFragment: AbstractLazyLoadFragment<FragmentListBinding>() {

    private val albumList: MutableList<Album> = mutableListOf()
    private val spanCount = 2
    private lateinit var homeActivity: HomeActivity
    private lateinit var viewModel: LocalMusicViewModel



    override fun FragmentListBinding.initView() {
        rvList.run {
            adapter = AlbumRecycleViewAdapter().apply {
                setOnItemClickListener {
                }
            }
            layoutManager = GridLayoutManager(context, spanCount)
            itemAnimator = DefaultItemAnimator()

        }
    }
    

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
        viewModel = (parentFragment as LocalMusicFragment).obtainViewModel()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe()
    }

    private fun subscribe() {
        viewModel.apply {
            albumItems.observe(homeActivity) {
                albumList.addAll(it)
                mBinding.rvList.adapter?.notifyDataSetChanged()
            }

            albumDataLoading.observe(homeActivity) { isLoading ->
                mBinding.rvList.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
                if (isLoading) {
                    mBinding.loadingView.show()
                } else {
                    mBinding.loadingView.hide()
                }
            }
        }
    }

    override fun getTAG(): String = AlbumListFragment::class.java.simpleName

    override fun onLoadData() {
        if (albumList.isEmpty()) {
            viewModel.loadAlbumList()
        }
    }

    override fun onPermissionFailed() {
        Toast.makeText(context, "permission deny", Toast.LENGTH_SHORT).show()
    }

    override fun getPermissions(): Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    inner class AlbumRecycleViewAdapter :
        BaseRecyclerViewAdapter<ItemAlbumListBinding, Album>(context, albumList) {
        override fun convert(
            holder: BaseViewHolder<ItemAlbumListBinding>,
            data: Album,
            position: Int
        ) {
            holder.mBinding.run {
                albumTitle.text = data.title
                albumArtist.text = data.artist
                albumCover.loadCover(data.id)
            }
        }
    }
}