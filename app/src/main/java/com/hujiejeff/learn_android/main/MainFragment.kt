package com.hujiejeff.learn_android.main

import androidx.core.view.ViewCompat
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.constraintlayout.ConstrainLayoutHomeDemoActivity
import com.hujiejeff.learn_android.databinding.FragmentMainBinding
import com.hujiejeff.learn_android.toolbar.ToolbarDemoFragment
import com.hujiejeff.learn_android.util.clickJump
import com.hujiejeff.learn_android.util.newInstance
import com.hujiejeff.learn_android.util.startFragmentByShareAnimator

class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun FragmentMainBinding.initView() {
        btnConstrainLayoutDemo.clickJump<ConstrainLayoutHomeDemoActivity>()
        ViewCompat.setTransitionName(mv,"ToolbarDemoFragment")
        mv.setOnClickListener {
            requireActivity().startFragmentByShareAnimator(
                newInstance<ToolbarDemoFragment>(),
                "Test",
                mv,
                "ToolbarDemoFragment"
            )
        }
    }
}