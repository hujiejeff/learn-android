package com.hujiejeff.learn_android.compose.component

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hujiejeff.learn_android.base.CommonApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PictureSelectorViewModel : ViewModel() {
    private val loadPicturesEvent = MutableStateFlow("")

    val picturesStateFlow: StateFlow<List<PictureBucket>> = loadPicturesEvent.transform {
        if (it.isNotEmpty()) {
            emit(providePictures().also { list ->
                currentBucketStateFlow.update {
                    list.first()
                }
            })
        }
    }.flowOn(Dispatchers.IO).stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed(5_000)
    )

    val currentBucketStateFlow = MutableStateFlow(PictureBucket("all", "最近项目", emptyList()))

    fun loadPictures() {
        viewModelScope.launch {
            loadPicturesEvent.emit("load") // 触发一次状态变化，可以用任何值
        }
    }

    fun selectAlbum(bucket: PictureBucket) {
        currentBucketStateFlow.update {
            bucket
        }
    }

    private fun providePictures(): List<PictureBucket> {
        val context = CommonApplication.get() as Context
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"  // 按图片添加的时间降序排序
        )

        val imageList = mutableListOf<PictureData>()
        val bucketList = mutableListOf<PictureBucket>()
        cursor?.use { cur ->
            val idColumn = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val bucketIdColumn = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn =
                cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            while (cur.moveToNext()) {
                val id = cur.getLong(idColumn)
                val displayName = cur.getString(displayNameColumn)
                val contentUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                val bucketId = cur.getString(bucketIdColumn)
                val bucketName = cur.getString(bucketNameColumn)
                val imageData = PictureData(
                    id,
                    displayName,
                    contentUri,
                    bucketId,
                    bucketName
                )  // 自定义的数据类，用于保存图片信息
                imageList.add(imageData)
            }
        }
        bucketList.add(PictureBucket("all", "最近项目", imageList))
        val categoryBucketList = imageList.groupBy { item -> item.bucketId }.toList().map { item ->
            val bucketId = item.first
            val bucketName = item.second.first().bucketName
            val list = item.second
            PictureBucket(bucketId, bucketName, list)
        }
        bucketList.addAll(categoryBucketList)
        return bucketList
    }
}

data class PictureData(
    val id: Long,
    val displayName: String,
    val contentUri: Uri,
    val bucketId: String,
    val bucketName: String
)

data class PictureBucket(val id: String, val name: String, val list: List<PictureData>)

const val BUCKETID_ALL = "ALL"
