package com.hujiejeff.musicplayer.discover.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.base.AbstractLazyLoadFragment
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.*
import com.hujiejeff.musicplayer.databinding.FragmentListBinding
import com.hujiejeff.musicplayer.databinding.ItemSearchResultAlbumBinding
import com.hujiejeff.musicplayer.databinding.ItemSearchResultArtistBinding
import com.hujiejeff.musicplayer.databinding.ItemSearchResultSongBinding
import com.hujiejeff.musicplayer.discover.playlist.PlaylistActivity
import com.hujiejeff.musicplayer.util.loadPlayListCover

/**
 * Create by hujie on 2020/3/6
 * 具体类型的搜索结果
 */
class SearchResultFragment<V: ViewBinding, D> : AbstractLazyLoadFragment<FragmentListBinding>() {

    companion object {
        fun <V: ViewBinding,D> newInstance(type: SearchType): SearchResultFragment<V, D> {
            val instance = SearchResultFragment<V,D>()
            instance.arguments = Bundle().apply {
                putInt("type", type.value)
            }
            return instance
        }
    }

    private lateinit var adapter: Adapter<V, D>
    private var type: SearchType? = null
    private lateinit var viewModel: SearchViewModel
    private val dataList: MutableList<D> = mutableListOf()
    private var itemLayoutID: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt("type")?.SearchType()
        viewModel.checkType(type!!)
        subscribe()
    }

    override fun getTAG(): String = "SearchResultFragment"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = (context as SearchActivity).obtainViewModel()
    }

    private fun subscribe() {
        viewModel.apply {
            searchLoadingMap[type]?.observe(this@SearchResultFragment, Observer {
                mBinding.loadingView.visibility = if (it) View.VISIBLE else View.INVISIBLE
                mBinding.rvList.visibility = if (it) View.INVISIBLE else View.VISIBLE
            })

            searchResultMap[type]?.observe(this@SearchResultFragment, Observer {
                dataList.clear()
                dataList.addAll(it as List<D>)
                mBinding.rvList.adapter?.notifyDataSetChanged()
            })
        }
    }

    override fun FragmentListBinding.initView() {
        rvList.apply {
            adapter = Adapter<V, D>(context, dataList).apply {
               setOnItemClickListener {i ->
                   when(val data = dataList[i]) {
                       is SearchPlayList -> {
                           PlaylistActivity.start(data.id, data.coverImgUrl, requireActivity())
                       }
                   }
               }
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onLoadData() {
        type?.apply {
            viewModel.loadSearchResult(this, 0, 10)
        }
    }

    override fun onPermissionFailed() {

    }

    override fun getPermissions(): Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )


    class Adapter<V: ViewBinding, D>(context: Context, datas: List<D>) :
        BaseRecyclerViewAdapter<V, D>(context, datas) {
        @SuppressLint("SetTextI18n")
        override fun convert(holder: BaseViewHolder<V>, data: D, position: Int) {
            when (data) {
                is SearchSong -> {
                    (holder.mBinding as ItemSearchResultSongBinding).run {
                        tvItemSongName.text = data.name
                        var string = ""
                        for (i in data.artists.indices) {
                            string += if (i != data.artists.size - 1) {
                                data.artists[i].name + "/"
                            } else {
                                data.artists[i].name
                            }
                        }
                        tvItemArtistName.text = string + " - " + data.album.name
                    }
                }

                is SearchArtist -> {
                    (holder.mBinding as ItemSearchResultArtistBinding).run {
                        tvSearchResultItemArtistName.text = data.name
                        val url = data.picUrl ?: ""
//                        civSearchResultItemArtistPic.loadPlayListCover(url)
                    }
                }

                is SearchAlbum -> {
                    var string = ""
                    for (i in data.artists.indices) {
                        string += if (i != data.artists.size - 1) {
                            data.artists[i].name + "/"
                        } else {
                            data.artists[i].name
                        }
                    }
                    (holder.mBinding as ItemSearchResultAlbumBinding).run {
                        tvSearchResultItemAlbumName.text = data.name
                        tvSearchResultItemAlbumArtist.text = string
                        ivSearchResultItemAlbumPic.loadPlayListCover(data.picUrl)
                    }
                }

                is SearchPlayList -> {
                    (holder.mBinding as ItemSearchResultAlbumBinding).run {
                        tvSearchResultItemAlbumName.text = data.name
                        tvSearchResultItemAlbumArtist.text =  "${data.trackCount}首 by ${data.creator.nickname}, 播放${data.playCount / 10000}万次"
                        ivSearchResultItemAlbumPic.loadPlayListCover(data.coverImgUrl)
                    }
                }

                is SearchUser -> {
                    (holder.mBinding as ItemSearchResultArtistBinding).run {
                        tvSearchResultItemArtistName.text = data.nickname
                        val url = data.avatarUrl ?: ""
//                        civSearchResultItemArtistPic.loadPlayListCover(data.coverImgUrl)
                    }
                }
            }
        }
    }


}