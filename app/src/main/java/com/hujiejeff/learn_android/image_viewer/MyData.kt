package com.hujiejeff.learn_android.image_viewer

import com.github.iielse.imageviewer.adapter.ItemType
import com.github.iielse.imageviewer.core.Photo

data class MyData(val url: String): Photo {
    override fun id(): Long = hashCode().toLong()

    override fun itemType(): Int = ItemType.PHOTO
}