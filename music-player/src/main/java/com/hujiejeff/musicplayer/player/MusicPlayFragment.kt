package com.hujiejeff.musicplayer.player

import android.content.Context
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.hujiejeff.musicplayer.base.App
import com.hujiejeff.musicplayer.base.BaseFragment
import com.hujiejeff.musicplayer.data.Preference
import com.hujiejeff.musicplayer.data.entity.Music
import com.hujiejeff.musicplayer.data.entity.PlayMode
import com.hujiejeff.musicplayer.databinding.CardAlbumBinding
import com.hujiejeff.musicplayer.databinding.FragmentMusicPlayBinding
import com.hujiejeff.musicplayer.util.getLocalCoverUrl
import com.hujiejeff.musicplayer.util.getMusicTimeFormatString
import com.hujiejeff.musicplayer.util.loadPlayListCover
import com.hujiejeff.musicplayer.util.logD

class MusicPlayFragment : BaseFragment<FragmentMusicPlayBinding>(),
    SeekBar.OnSeekBarChangeListener {

    private val fragmentList = mutableListOf<Fragment>()

    private var isFirst = true

    private lateinit var viewModel: PlayerViewModel


    override fun FragmentMusicPlayBinding.initView() {

        viewModel.currentMusic.value?.let {
            logD("updateUI")
            updateUI(it)
            seekBar.progress = Preference.play_progress
        }
        //attach前尚未订阅，所以无法接收消息改变UI
        ivPlayBtnPlay.isSelected = viewModel.isPlay.value!!

        initClickListener()
        root.isClickable = true
        //viewpager
        viewModel.musicItems.value?.forEach {
            fragmentList.add(
                AlbumCardFragment(
                    getLocalCoverUrl(it.albumID)
                )
            )
        }
        playViewPager.run {
            adapter = PlayAlbumPagerAdapter()
            setPageTransformer(
                true,
                ScaleTransformer()
            )
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    //TODO 第一次
                    if (!isFirst) {
                        mBinding.root.handler.postDelayed({
                            viewModel.play(position)
                        }, 500)

                    }
                    isFirst = false
                }
            })
            currentItem = viewModel.playPosition
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = App.playerViewModel
        subscribe()
    }

    //初始按钮监听
    private fun FragmentMusicPlayBinding.initClickListener() {
        ivPlayModeLoop.setOnClickListener {
            viewModel.changeLoopMode()
        }

        ivPlayModeShuffle.setOnClickListener {
            viewModel.changeShuffleMode()
        }

        ivPalyBtnPrev.setOnClickListener {
            viewModel.pre()
        }
        ivPlayBtnNext.setOnClickListener {
            viewModel.next()
        }
        ivPlayBtnPlay.setOnClickListener {
            viewModel.playOrPause()
        }

        seekBar.setOnSeekBarChangeListener(this@MusicPlayFragment)
        seekBar.setOnClickListener {
            //屏蔽点击
        }

        ivPlayBtnClose.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    //订阅消息改变UI
    private fun subscribe() {
        viewModel.apply {
            //music change
            currentMusic.observe(this@MusicPlayFragment) {
                mBinding.updateUI(it)
            }

            //music state
            isPlay.observe(this@MusicPlayFragment) {
                mBinding.ivPlayBtnPlay.isSelected = it
            }

            //music progress
            playProgress.observe(this@MusicPlayFragment) { progress ->
                mBinding.seekBar.progress = progress
                mBinding.tvCurrentTime.text = getMusicTimeFormatString(progress)
            }

            //music buffer progress
            bufferProgress.observe(this@MusicPlayFragment) { bufferProgress ->
                mBinding.seekBar.secondaryProgress = bufferProgress
            }

            //music mode
            playMode.observe(this@MusicPlayFragment) {
                mBinding.run {
                    when (it) {
                        PlayMode.SINGLE, PlayMode.LOOP, PlayMode.SINGLE_LOOP -> {
                            ivPlayModeLoop.setImageLevel(it.value)
                            ivPlayModeShuffle.isSelected = false
                        }
                        PlayMode.SHUFFLE -> {
                            ivPlayModeLoop.setImageLevel(0)
                            ivPlayModeShuffle.isSelected = true
                        }
                        else -> {
                        }
                    }
                }

            }

            position.observe(this@MusicPlayFragment) { index ->
                mBinding.playViewPager.setCurrentItem(index, true)
            }

            musicItems.observe(this@MusicPlayFragment) { list ->
                fragmentList.clear()
                list.forEach { music ->
                    val src =
                        if (music.type == 0) getLocalCoverUrl(music.albumID) else music.coverSrc
                    fragmentList.add(
                        AlbumCardFragment(src!!)
                    )
                }
                mBinding.playViewPager.adapter?.notifyDataSetChanged()
            }
        }


    }

    private fun FragmentMusicPlayBinding.updateUI(music: Music) {
        music.apply {
            tvPlayTitle.text = title
            tvPlayArtist.text = artist
            seekBar.max = duration.toInt()
            seekBar.progress = 0
            tvCurrentTime.text = getMusicTimeFormatString(Preference.play_progress)
            tvMaxTime.text = getMusicTimeFormatString(duration.toInt())
        }
        val isShuffle = Preference.play_mode == PlayMode.SHUFFLE.value
        ivPlayModeLoop.setImageLevel(if (isShuffle) 0 else Preference.play_mode)
        ivPlayModeShuffle.isSelected = isShuffle
    }

    class AlbumCardFragment(private val src: String) : BaseFragment<CardAlbumBinding>() {
        override fun CardAlbumBinding.initView() {
            albumCover.loadPlayListCover(src)
        }
    }

    inner class PlayAlbumPagerAdapter :
        FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = fragmentList[position]
        override fun getCount() = fragmentList.size
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        mBinding.tvCurrentTime.text = getMusicTimeFormatString(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        viewModel.seekTo(seekBar!!.progress)
    }
}