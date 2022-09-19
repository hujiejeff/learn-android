package com.hujiejeff.learn_android.main

import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.materia_design.constraintlayout.ConstrainLayoutHomeDemoActivity
import com.hujiejeff.learn_android.databinding.FragmentMainBinding
import com.hujiejeff.learn_android.jetpack.vm.LoginActivity
import com.hujiejeff.learn_android.jetpack.vm.LoginViewModel
import com.hujiejeff.learn_android.materia_design.toolbar.ToolbarDemoFragment
import com.hujiejeff.learn_android.util.clickJump
import com.hujiejeff.learn_android.util.newInstance
import com.hujiejeff.learn_android.util.startFragmentByShareAnimator

class MainFragment : BaseFragment<FragmentMainBinding>() {
    val viewModel: LoginViewModel by activityViewModels()

    override fun FragmentMainBinding.initView() {
        btnConstrainLayoutDemo.clickJump<ConstrainLayoutHomeDemoActivity>()
        ViewCompat.setTransitionName(btnToolbarDemo,"ToolbarDemoFragment")
        btnToolbarDemo.setOnClickListener {
            requireActivity().startFragmentByShareAnimator(
                newInstance<ToolbarDemoFragment>(),
                "Test",
                btnToolbarDemo,
                "ToolbarDemoFragment"
            )
        }
        btnMvvmDemo.clickJump<LoginActivity>()
    }
}