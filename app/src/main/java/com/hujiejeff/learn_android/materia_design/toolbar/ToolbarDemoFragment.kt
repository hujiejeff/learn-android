package com.hujiejeff.learn_android.materia_design.toolbar

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.core.view.ViewCompat
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.databinding.FragmentTollbarDemoBinding

class ToolbarDemoFragment: BaseFragment<FragmentTollbarDemoBinding>() {
    override fun FragmentTollbarDemoBinding.initView() {
        ViewCompat.setTransitionName(root, "ToolbarDemoFragment")
        ivClipTest.clipToOutline = true
        ivClipTest.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(50, 50, view.width, view.height)
            }
        }
    }
}