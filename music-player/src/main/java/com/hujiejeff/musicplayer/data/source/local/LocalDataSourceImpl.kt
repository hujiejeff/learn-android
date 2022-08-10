package com.hujiejeff.musicplayer.data.source.local

import com.hujiejeff.musicplayer.component.AppExecutors
import com.hujiejeff.musicplayer.data.Preference
import com.hujiejeff.musicplayer.data.entity.Album
import com.hujiejeff.musicplayer.data.entity.Artist
import com.hujiejeff.musicplayer.data.entity.Music
import com.hujiejeff.musicplayer.data.source.Callback
import com.hujiejeff.musicplayer.data.source.LocalDataSource
import com.hujiejeff.musicplayer.util.getAlbumList
import com.hujiejeff.musicplayer.util.getArtistList
import com.hujiejeff.musicplayer.util.getMusicList

/**
 * Create by hujie on 2020/1/3
 */
class LocalDataSourceImpl(val appExecutors: AppExecutors): LocalDataSource {

    override fun getLocalMusicList(callback: Callback<List<Music>>) {
        appExecutors.diskIO.execute {
            Thread.sleep(3000) //模拟耗时操作
            val list = getMusicList()
            appExecutors.mainThread.execute {
                if (list.isEmpty()) {
                    callback.onFailed("is empty")
                } else {
                    callback.onLoaded(list)
                }
            }
        }
    }

    override fun getLocalAlbumList(callback: Callback<List<Album>>) {
        appExecutors.diskIO.execute {
            Thread.sleep(3000) //模拟耗时操作
            val list = getAlbumList()
            appExecutors.mainThread.execute {
                if (list.isEmpty()) {
                    callback.onFailed("is empty")
                } else {
                    callback.onLoaded(list)
                }
            }
        }
    }

    override fun getLocalArtistList(callback: Callback<List<Artist>>) {
        appExecutors.diskIO.execute {
            Thread.sleep(3000) //模拟耗时操作
            val list = getArtistList()
            appExecutors.mainThread.execute {
                if (list.isEmpty()) {
                    callback.onFailed("is empty")
                } else {
                    callback.onLoaded(list)
                }
            }
        }
    }

    override fun getSearchHistorySet(callback: Callback<Set<String>>) {
        callback.onLoaded(Preference.searchHistory)
    }

    override fun saveSearchHistorySet(historySet: Set<String>) {
        Preference.searchHistory = historySet
    }

}