package com.hujiejeff.learn_android.material3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityButtonDemoBinding

class ButtonDemoActivity : BaseActivity<ActivityButtonDemoBinding>() {
    override fun ActivityButtonDemoBinding.initView() {
        btnGroup.isSelectionRequired = true
        btnGroup.isSingleSelection = true
    }
}