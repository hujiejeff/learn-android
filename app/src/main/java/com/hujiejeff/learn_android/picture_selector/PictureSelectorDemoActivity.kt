package com.hujiejeff.learn_android.picture_selector

import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.core.SimpleDataProvider
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityPictureSelectorDemoBinding
import com.hujiejeff.learn_android.image_viewer.MyData
import com.hujiejeff.learn_android.image_viewer.SimpleImageLoader
import com.hujiejeff.learn_android.image_viewer.SimpleTransformer
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener


class PictureSelectorDemoActivity : BaseActivity<ActivityPictureSelectorDemoBinding>() {
    private val data = mutableListOf<LocalMedia>()
    private val dataList = mutableListOf<Photo>()
    private lateinit var clickedData: Photo
    override fun ActivityPictureSelectorDemoBinding.initView() {
        btnOpenPictureSelector.setOnClickListener {
            openPictureSelector()
        }

        ivSelected.setOnClickListener {
            clickedData = MyData(data[0].availablePath)
            SimpleTransformer.put(clickedData.id(), ivSelected)
            show()
            dataList.clear()
            dataList.add(clickedData)
        }
    }

    private fun openPictureSelector() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.get())
            .setSelectedData(data)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    GlideEngine.get().loadImage(
                        this@PictureSelectorDemoActivity,
                        result[0].availablePath,
                        mBinding.ivSelected
                    )
                    data.clear()
                    data.addAll(result)
                }

                override fun onCancel() {}
            })

    }

    private fun show() {
        val builder = ImageViewerBuilder(
            context = this,
            dataProvider = SimpleDataProvider(
                clickedData,
                dataList
            ), // 一次性全量加载 // 实现DataProvider接口支持分页加载
            imageLoader = SimpleImageLoader(), // 可使用demo固定写法 // 实现对数据源的加载.支持自定义加载数据类型，加载方案
            transformer = SimpleTransformer(), // 可使用demo固定写法 // 以photoId为标示，设置过渡动画的'配对'.
        )
        builder.show()
    }
}