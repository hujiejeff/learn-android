package com.hujiejeff.learn_android.main

import android.app.AlertDialog
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.databinding.DialogSampleBinding
import com.hujiejeff.learn_android.databinding.FragmentMainBinding
import com.hujiejeff.learn_android.jetpack.vm.LoginActivity
import com.hujiejeff.learn_android.jetpack.vm.LoginViewModel
import com.hujiejeff.learn_android.materia_design.constraintlayout.ConstrainLayoutHomeDemoActivity
import com.hujiejeff.learn_android.materia_design.toolbar.ToolbarDemoFragment
import com.hujiejeff.learn_android.picture_selector.PictureSelectorDemoActivity
import com.hujiejeff.learn_android.saf.NewStorageTestActivity
import com.hujiejeff.learn_android.transition.TransitionDemoActivity
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
        btnOpenAlertDialog.setOnClickListener {
            val dialogBinding = DialogSampleBinding.inflate(LayoutInflater.from(context))
            val dialog =
                AlertDialog.Builder(context)
                    .setView(dialogBinding.root)
                    .setCancelable(true)
                    .create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) //消除白块
            dialog.show()
        }
        btnSwitchTheme.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        btnOpenDialogFragment.setOnClickListener {
            Custom3DialogFragment().show(childFragmentManager, "")
        }
        btnOpenNewStorageApiActivity.clickJump<NewStorageTestActivity>()
        btnOpenPictureSelectorDemo.clickJump<PictureSelectorDemoActivity>()
        btnOpenTransitionDemo.clickJump<TransitionDemoActivity>()
    }
}