package com.hujiejeff.musicplayer.discover.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.hujiejeff.musicplayer.R
import com.hujiejeff.musicplayer.base.BaseActivity
import com.hujiejeff.musicplayer.base.BaseRecyclerViewAdapter
import com.hujiejeff.musicplayer.base.BaseViewHolder
import com.hujiejeff.musicplayer.data.entity.HotSearchString
import com.hujiejeff.musicplayer.data.entity.RecommendNewAlbum
import com.hujiejeff.musicplayer.data.entity.RecommendNewSong
import com.hujiejeff.musicplayer.data.entity.RecommendPlayList
import com.hujiejeff.musicplayer.databinding.*
import com.hujiejeff.musicplayer.util.dp2Px
import com.hujiejeff.musicplayer.util.loadPlayListCover
import com.hujiejeff.musicplayer.util.obtainViewModel
import com.hujiejeff.musicplayer.util.transaction

/**
 * Create by hujie on 2020/3/4
 */
class SearchActivity : BaseActivity() {
    private val hotSearchStringList = mutableListOf<HotSearchString>()
    private val searchHistoryStringList = mutableListOf<String>()
    private lateinit var viewModel: SearchViewModel
    private val containerFragment = SearchResultContainerFragment()
    override fun layoutResId(): Int = R.layout.activity_search
    private lateinit var binding: ActivitySearchBinding

    override fun isLightStatusBar(): Boolean = true
    override fun getViewBinding(): View {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvHistory.apply {
            adapter = createAdapter<ItemSearchHistoryBinding, String>(searchHistoryStringList).apply {
                setOnItemClickListener {
                    viewModel.startSearch(searchHistoryStringList[it])
                }
            }
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
        }


        binding.rvHotSearch.apply {
            adapter = createAdapter<ItemHotSearchSongBinding, HotSearchString>( hotSearchStringList).apply {
                setOnItemClickListener {
                    viewModel.startSearch(hotSearchStringList[it].first)
                }
            }
            itemAnimator = DefaultItemAnimator()
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        viewModel = obtainViewModel()

        subscribe()

        binding.includeView.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.keyCode && KeyEvent.ACTION_DOWN == keyEvent.action) {
                viewModel.startSearch(textView.text.toString())

                return@setOnEditorActionListener true
            }
            false
        }

        binding.includeView.etSearch.setOnClickListener {
            (it as EditText).isCursorVisible = true
        }

        binding.ivSearchHistoryClear.setOnClickListener {
            viewModel.clearHistory()
        }

       transaction {
           add(R.id.fl_container, containerFragment)
           hide(containerFragment)
       }

        viewModel.loadHistory()
        viewModel.loadHotSearch()

    }


    private fun subscribe() {
        viewModel.apply {
            loading.observe(this@SearchActivity, Observer { isLoading ->
                binding.lvLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.flContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
            })

            hotSearch.observe(this@SearchActivity, Observer {
                hotSearchStringList.clear()
                hotSearchStringList.addAll(it)
                binding.rvHotSearch.adapter?.notifyDataSetChanged()
            })

            searchHistory.observe(this@SearchActivity, Observer {
                searchHistoryStringList.clear()
                searchHistoryStringList.addAll(it)
                binding.rvHistory.adapter?.notifyDataSetChanged()
            })

            currentSearchKey.observe(this@SearchActivity, Observer {
                binding.includeView.etSearch.setText(it)
                binding.includeView.etSearch.isCursorVisible = false
                transaction {
                    show(containerFragment)
                }
            })
        }
    }

    fun obtainViewModel() = obtainViewModel(SearchViewModel::class.java)

    private inline fun <reified V : ViewBinding, D> createAdapter(datas: List<D>): BaseRecyclerViewAdapter<V, D> {
        return object : BaseRecyclerViewAdapter<V, D>(this, datas) {
            @SuppressLint("SetTextI18n")
            override fun convert(holder: BaseViewHolder<V>, data: D, position: Int) {
                when (data) {
                    is String -> {
                        (holder.mBinding as ItemSearchHistoryBinding).tvHistoryString.text = data
                    }
                    is HotSearchString -> {
                        (holder.mBinding as ItemHotSearchSongBinding).run {
                            tvHotSearchIndex.text = (position + 1).toString()
                            tvHotSearchString.text = data.first
                        }
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        if (!containerFragment.isHidden) {
            transaction {
                hide(containerFragment)
            }
            return
        }
        super.onBackPressed()
    }
}