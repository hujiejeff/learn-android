package com.hujiejeff.learn_android.picture_selector

import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityPictureSelectorDemoBinding
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener


class PictureSelectorDemoActivity: BaseActivity<ActivityPictureSelectorDemoBinding>() {
    private val data = mutableListOf<LocalMedia>()
    override fun ActivityPictureSelectorDemoBinding.initView() {
        btnOpenPictureSelector.setOnClickListener {
            openPictureSelector()
        }
    }

    private fun openPictureSelector() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.get())
            .setSelectedData(data)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    GlideEngine.get().loadImage(this@PictureSelectorDemoActivity, result[0].availablePath, mBinding.ivSelected)
                    data.clear()
                    data.addAll(result)
                }
                override fun onCancel() {}
            })

    }
}