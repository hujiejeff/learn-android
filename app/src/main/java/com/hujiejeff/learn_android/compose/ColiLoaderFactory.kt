package com.hujiejeff.learn_android.compose

import coil.ImageLoader
import coil.ImageLoaderFactory
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.CommonApplication
import com.hujiejeff.learn_android.coli.CoilLoggingInterceptor

class ColiLoaderFactoryImpl : ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(CommonApplication.get())
            .crossfade(true)
            .allowHardware(true)
            .placeholder(R.drawable.shape_img_place_holder)
            .fallback(R.drawable.shape_img_place_holder)
            .error(R.drawable.shape_img_place_holder)
            .components {
                add(CoilLoggingInterceptor())
            }
            .build()
    }
}