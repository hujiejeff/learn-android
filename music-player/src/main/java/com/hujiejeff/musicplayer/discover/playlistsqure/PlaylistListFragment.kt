package com.hujiejeff.musicplayer.discover.playlistsqure

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.base.AbstractLazyLoadFragment
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.PlayList
import com.hujiejeff.musicplayer.data.entity.SubCat
import com.hujiejeff.musicplayer.databinding.FragmentListBinding
import com.hujiejeff.musicplayer.databinding.ItemPlaylistListBinding
import com.hujiejeff.musicplayer.discover.playlist.PlaylistActivity
import com.hujiejeff.musicplayer.util.loadPlayListCover
import com.hujiejeff.musicplayer.util.logD

/**desc: 歌单列表，3x3列grid
 * Create by hujie on 2020/1/10
 */

class PlaylistListFragment : AbstractLazyLoadFragment<FragmentListBinding>() {

    lateinit var subCat: SubCat
    private var playLists: MutableList<PlayList> = mutableListOf()
    private lateinit var viewModel: MainPlaylistViewModel

    private lateinit var activity: MainPlaylistActivity


    override fun FragmentListBinding.initView() {
        rvList.apply {
            adapter = Adapter().apply {
                setOnItemClickListener {
                    PlaylistActivity.start( playLists[it].id, playLists[it].coverImgUrl, activity)
                }
            }
            layoutManager = GridLayoutManager(context, 3)
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun getTAG(): String = PlaylistListFragment::class.java.simpleName

    override fun onPermissionFailed() {
        logD("onPermissionFailed")
    }

    override fun getPermissions(): Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainPlaylistActivity
        viewModel = activity.obtainViewModel()
        subscriber()
    }

    private fun subscriber() {
        viewModel.loadingMap[subCat]?.observe(this, Observer { isLoading ->
            mBinding.rvList.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            if (isLoading) {
                mBinding.loadingView.show()
            } else {
                mBinding.loadingView.hide()
            }
        })
        viewModel.playListMap[subCat]?.observe(this, Observer {
            playLists.addAll(it)
            mBinding.rvList.adapter?.notifyDataSetChanged()
        })
    }

    override fun onLoadData() {
        viewModel.loadPlaylists(subCat)
    }


    inner class Adapter :
        BaseRecyclerViewAdapter<ItemPlaylistListBinding, PlayList>(context, playLists) {
        @SuppressLint("SetTextI18n")
        override fun convert(holder: BaseViewHolder<ItemPlaylistListBinding>, data: PlayList, position: Int) {
            holder.mBinding.run {
                tvPlaylistName.text =  data.name
                tvPlaylistPlayCount.text =  (data.playCount / 10000).toString() + "万"
                ivPlaylistCover.loadPlayListCover(data.coverImgUrl)
            }
        }
    }
}