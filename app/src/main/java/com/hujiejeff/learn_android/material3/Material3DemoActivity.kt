package com.hujiejeff.learn_android.material3

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.base.CommonApplication
import com.hujiejeff.learn_android.databinding.ActivityMaterial3DemoBinding

class Material3DemoActivity: BaseActivity<ActivityMaterial3DemoBinding>() {
    override fun ActivityMaterial3DemoBinding.initView() {
        swUseMaterial3.isChecked = CommonApplication.get().isMaterial3Theme()
        swUseMaterial3.setOnCheckedChangeListener { buttonView, isChecked ->
            CommonApplication.get().setMaterial3Theme(isChecked)
            AppUtils.relaunchApp()
        }


        BarUtils.setStatusBarLightMode(window, true)
        appbarLayout.statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(this@Material3DemoActivity)
        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            onBackPressed()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            root
        ) { v: View?, insetsCompat: WindowInsetsCompat ->
            val insets = insetsCompat.getInsets(WindowInsetsCompat.Type.statusBars())
            appbarLayout.setPadding(0, insets.top, 0, 0)
            insetsCompat
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    SnackbarUtils.with(root).setMessage("Click edit").show()
                    // Handle edit text press
                    true
                }
                R.id.favorite -> {
                    SnackbarUtils.with(root).setMessage("Click favorite").show()
                    // Handle favorite icon press
                    true
                }
                R.id.more -> {
                    SnackbarUtils.with(root).setMessage("Click more").show()
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }
    }
}