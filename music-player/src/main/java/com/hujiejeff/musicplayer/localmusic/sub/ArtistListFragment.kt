package com.hujiejeff.musicplayer.localmusic.sub

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.hujiejeff.musicplayer.HomeActivity
import com.hujiejeff.musicplayer.base.AbstractLazyLoadFragment
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.Artist
import com.hujiejeff.musicplayer.databinding.FragmentListBinding
import com.hujiejeff.musicplayer.databinding.ItemArtistListBinding
import com.hujiejeff.musicplayer.localmusic.LocalMusicFragment
import com.hujiejeff.musicplayer.localmusic.LocalMusicViewModel
import com.hujiejeff.musicplayer.util.getArtistCover

class ArtistListFragment : AbstractLazyLoadFragment<FragmentListBinding>() {

    private val artistList: MutableList<Artist> = mutableListOf()
    private val spanCount = 2
    private lateinit var homeActivity: HomeActivity
    private lateinit var viewModel: LocalMusicViewModel


    override fun FragmentListBinding.initView() {
        rvList.run {
            adapter = ArtistRecycleViewAdapter().apply {
                setOnItemClickListener {
                }
            }
            layoutManager = GridLayoutManager(context, spanCount)
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

            artistItems.observe(homeActivity, Observer {
                artistList.addAll(it)
                mBinding.rvList.adapter?.notifyDataSetChanged()
            })


            artistDataLoading.observe(homeActivity, Observer { isLoading ->
                mBinding.rvList.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
                if (isLoading) {
                    mBinding.loadingView.show()
                } else {
                    mBinding.loadingView.hide()
                }
            })
        }
    }

    override fun getTAG(): String = ArtistListFragment::class.java.simpleName

    override fun onLoadData() {
        if (artistList.isEmpty()) {
            viewModel.loadArtistList()
        }
    }

    override fun onPermissionFailed() {
        Toast.makeText(context, "permission deny", Toast.LENGTH_SHORT).show()
    }

    override fun getPermissions(): Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    inner class ArtistRecycleViewAdapter :
        BaseRecyclerViewAdapter<ItemArtistListBinding, Artist>(context, artistList) {
        override fun convert(
            holder: BaseViewHolder<ItemArtistListBinding>,
            data: Artist,
            position: Int
        ) {
            holder.mBinding.run {
                artistCover.setImageBitmap(getArtistCover())
                artistName.text = data.name
            }
        }
    }
}