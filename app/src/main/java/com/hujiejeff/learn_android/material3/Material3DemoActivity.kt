package com.hujiejeff.learn_android.material3

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.google.common.eventbus.EventBus
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.base.CommonApplication
import com.hujiejeff.learn_android.databinding.ActivityConstrainLayoutHomeDemoBinding
import com.hujiejeff.learn_android.databinding.ActivityMaterial3DemoBinding

class Material3DemoActivity: BaseActivity<ActivityMaterial3DemoBinding>() {
    override fun ActivityMaterial3DemoBinding.initView() {
        swUseMaterial3.isChecked = CommonApplication.get().isMaterial3Theme()
        swUseMaterial3.setOnCheckedChangeListener { buttonView, isChecked ->
            CommonApplication.get().setMaterial3Theme(isChecked)
            AppUtils.relaunchApp()
        }
    }
}