package com.hujiejeff.learn_android.image_viewer

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.iielse.imageviewer.core.ImageLoader
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.widgets.video.ExoVideoView2

// 基本是固定写法. Glide 可以换成别的. demo代码中有video的写法.
class SimpleImageLoader : ImageLoader {
    override fun load(view: ImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
        super.load(view, data, viewHolder)
        val it = (data as? MyData?)?.url ?: return
        Glide.with(view).load(it)
            .placeholder(view.drawable)
            .into(view)
    }
}