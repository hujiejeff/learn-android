package com.hujiejeff.learn_android.toolbar

import androidx.core.view.ViewCompat
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.databinding.FragmentTollbarDemoBinding

class ToolbarDemoFragment: BaseFragment<FragmentTollbarDemoBinding>() {
    override fun FragmentTollbarDemoBinding.initView() {
        ViewCompat.setTransitionName(root, "ToolbarDemoFragment")
    }
}