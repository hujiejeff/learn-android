package com.hujiejeff.learn_android.shizuku

import android.content.pm.PackageManager
import android.os.Bundle
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityShizukuDemoBinding


class ShizukuDemoActivity: BaseActivity<ActivityShizukuDemoBinding>() {
    override fun ActivityShizukuDemoBinding.initView() {
        btnRequestPermission.setOnClickListener {

        }
        btnDo.setOnClickListener {

        }
    }


    private fun onRequestPermissionsResult(requestCode: Int, grantResult: Int) {
        val granted = grantResult == PackageManager.PERMISSION_GRANTED
        // Do stuff based on the result and the request code
    }

}