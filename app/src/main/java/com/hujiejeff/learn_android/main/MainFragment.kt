package com.hujiejeff.learn_android.main

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.SnackbarUtils
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.compose.codelab_animation.DemoActivity
import com.hujiejeff.learn_android.compose.codelab_basic.ComposeDemo2Activity
import com.hujiejeff.learn_android.compose.codelab_basic_layout.CodeLabActivity
import com.hujiejeff.learn_android.compose.codelab_quickuse.ComposeDemoActivity
import com.hujiejeff.learn_android.databinding.DialogSampleBinding
import com.hujiejeff.learn_android.databinding.FragmentMainBinding
import com.hujiejeff.learn_android.jetpack.vm.LoginActivity
import com.hujiejeff.learn_android.jetpack.vm.LoginViewModel
import com.hujiejeff.learn_android.materia_design.constraintlayout.ConstrainLayoutHomeDemoActivity
import com.hujiejeff.learn_android.materia_design.toolbar.ToolbarDemoFragment
import com.hujiejeff.learn_android.material3.Material3DemoActivity
import com.hujiejeff.learn_android.mytool.MyToolActivity
import com.hujiejeff.learn_android.picture_selector.PictureSelectorDemoActivity
import com.hujiejeff.learn_android.saf.NewStorageTestActivity
import com.hujiejeff.learn_android.transition.TransitionDemoActivity
import com.hujiejeff.learn_android.util.clickJump
import com.hujiejeff.learn_android.util.newInstance
import com.hujiejeff.learn_android.util.startFragmentByShareAnimator


class MainFragment : BaseFragment<FragmentMainBinding>() {
    val viewModel: LoginViewModel by activityViewModels()
    val viewModel2: LoginViewModel by viewModels()
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

        btnOpenShizukuDemo.setOnClickListener {
//            SnackbarUtils.with(root).setBgColor(0xffff0000.toInt()).show(true)
            SnackbarUtils.with(root).setBgResource(R.drawable.snack_bar_bg).setMessage("测试").show()
        }
        btnOpenNewStorageApiActivity.clickJump<NewStorageTestActivity>()
        btnOpenPictureSelectorDemo.clickJump<PictureSelectorDemoActivity>()
        btnOpenTransitionDemo.clickJump<TransitionDemoActivity>()
        btnOpenMaterial3Demo.clickJump<Material3DemoActivity>()

        shapeBtn.setOnClickListener {
            shapeBtn.shapeDrawableBuilder
                .setSolidColor(-0x1000000)
                .setStrokeColor(-0xa57221) // 注意：最后需要调用一下 intoBackground 方法才能生效
                .intoBackground()
            shapeBtn.textColorBuilder
                .setTextColor(-0x1) // 注意：最后需要调用一下 intoTextColor 方法才能生效
                .intoTextColor()
            shapeBtn.text = "颜色已经2222改变啦22222"
        }
        btnOpenComposeDemo.clickJump<ComposeDemoActivity>()
        btnOpenComposeDemo2.clickJump<ComposeDemo2Activity>()
        btnOpenComposeDemo3.clickJump<CodeLabActivity>()
        btnOpenComposeDemo4.clickJump<DemoActivity>()
        btnOpenComposeDemoNavigation.clickJump<com.hujiejeff.learn_android.compose.navigation.DemoActivity>()
        btnOpenToolApp.clickJump<MyToolActivity>()
    }
}