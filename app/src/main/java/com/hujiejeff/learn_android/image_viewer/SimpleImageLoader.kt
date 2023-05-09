package com.hujiejeff.learn_android.image_viewer

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.github.iielse.imageviewer.core.ImageLoader
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.widgets.video.ExoVideoView2
import com.hujiejeff.learn_android.picture_selector.GlideEngine

// 基本是固定写法. Glide 可以换成别的. demo代码中有video的写法.
class SimpleImageLoader : ImageLoader {
    override fun load(view: ImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
        val it = (data as? MyData?)?.url ?: return
        val url ="https://oss-media.dutenews.com/2023/04/25/7cS4UblmCpFdcjIatZ6I1AKppOuIr9wLU2EPyL64VMN0uDbZf6yihNPANHFyqyk0.png"
        GlideEngine.get().loadImage(view.context, url, view)
        Glide.with(view).load("https://oss-media.dutenews.com/2023/04/25/7cS4UblmCpFdcjIatZ6I1AKppOuIr9wLU2EPyL64VMN0uDbZf6yihNPANHFyqyk0.png")
            .placeholder(view.drawable)
            .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
            .into(view)
    }
}