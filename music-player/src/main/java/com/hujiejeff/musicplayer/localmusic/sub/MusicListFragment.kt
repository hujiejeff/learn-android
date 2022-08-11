package com.hujiejeff.musicplayer.localmusic.sub

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hujiejeff.musicplayer.HomeActivity
import com.hujiejeff.musicplayer.base.AbstractLazyLoadFragment
import com.hujiejeff.musicplayer.base.App
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.Music
import com.hujiejeff.musicplayer.databinding.FragmentListBinding
import com.hujiejeff.musicplayer.databinding.ItemMusicListBinding
import com.hujiejeff.musicplayer.localmusic.LocalMusicFragment
import com.hujiejeff.musicplayer.localmusic.LocalMusicViewModel
import com.hujiejeff.musicplayer.util.loadCover
import com.hujiejeff.musicplayer.util.logD

class MusicListFragment : AbstractLazyLoadFragment<FragmentListBinding>() {

    private val musicList: MutableList<Music> = mutableListOf()
    private lateinit var homeActivity: HomeActivity
    private lateinit var viewModel: LocalMusicViewModel


    override fun FragmentListBinding.initView() {
        rvList.apply {
            adapter = MusicRecyclerViewAdapter().apply {
                setOnItemClickListener {

                    App.playerViewModel.play(it)
                }
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = activity as HomeActivity
        viewModel = (parentFragment as LocalMusicFragment).obtainViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe()
    }

    private fun subscribe() {
        viewModel.apply {
            musicItems.observe(homeActivity, Observer<List<Music>> { list ->
                musicList.addAll(list)
                mBinding.rvList.adapter?.notifyDataSetChanged()
                logD("observer")
                App.playerViewModel.loadDefaultMusic()
            })

            isDataLoadingError.observe(homeActivity, Observer { isError ->
                if (isError) Toast.makeText(
                    homeActivity,
                    "load error",
                    Toast.LENGTH_SHORT
                ).show()
            })

            musicDataLoading.observe(homeActivity, Observer { isLoading ->
                mBinding.rvList.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
                if (isLoading) {
                    mBinding.loadingView.show()
                } else {
                    mBinding.loadingView.hide()
                }
            })
        }
    }

    override fun getTAG(): String = MusicListFragment::class.java.simpleName

    override fun onLoadData() {
        if (musicList.isEmpty())
            viewModel.loadMusicList()
    }

    override fun onPermissionFailed() {
        Toast.makeText(context, "permission deny", Toast.LENGTH_SHORT).show()
    }

    override fun getPermissions(): Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    inner class MusicRecyclerViewAdapter :
        BaseRecyclerViewAdapter<ItemMusicListBinding, Music>(context, musicList) {
        override fun convert(
            holder: BaseViewHolder<ItemMusicListBinding>,
            data: Music,
            position: Int
        ) {
            holder.mBinding.run {
                tvMusicTitle.text = data.title
                tvMusicArtist.text = data.artist
                ivMusicCover.loadCover(data.albumID)
            }
        }
    }
}