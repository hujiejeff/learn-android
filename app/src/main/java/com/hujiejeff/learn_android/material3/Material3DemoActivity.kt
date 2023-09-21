package com.hujiejeff.learn_android.material3

import android.graphics.Color
import android.transition.Transition
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintProperties
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.core.graphics.toColorLong
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.customview.widget.ViewDragHelper
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.elevation.SurfaceColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.base.CommonApplication
import com.hujiejeff.learn_android.databinding.ActivityMaterial3DemoBinding
import com.hujiejeff.learn_android.util.clickJump
import com.hujiejeff.learn_android.util.jump

class Material3DemoActivity : BaseActivity<ActivityMaterial3DemoBinding>() {
    private var dialogFragment: DialogDemoFragment? = null

    override fun ActivityMaterial3DemoBinding.initView() {
        composeView.setContent {
            ElevatedCard(modifier = Modifier.size(100.dp, 200.dp)) {
                Text("A composable view in Android View")
            }
        }

        swUseMaterial3.isChecked = CommonApplication.get().isMaterial3Theme()
        swUseMaterial3.setOnCheckedChangeListener { buttonView, isChecked ->
            CommonApplication.get().setMaterial3Theme(isChecked)
            AppUtils.relaunchApp()
        }


        BarUtils.setStatusBarLightMode(window, true)
        appbarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(this@Material3DemoActivity)
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

        btnToMdButton.clickJump<ButtonDemoActivity>()
        filledBtn.setOnClickListener {
            val color = SurfaceColors.SURFACE_0.getColor(this@Material3DemoActivity)
            SnackbarUtils.with(root).setAction("fdafd") {
                ToastUtils.showShort("dfdafd")
            }.setMessage("测试").show()
//            SnackbarUtils.with(root).setBgColor(color).setMessage(ColorUtils.int2ArgbString(color)).show()

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            SnackbarUtils.with(root).setBgColor(Color.TRANSPARENT)
                .setDuration(SnackbarUtils.LENGTH_INDEFINITE).show()
            SnackbarUtils.addView(R.layout.snackbar_custom, params)
        }

        elevatedBtn.setOnClickListener {
            SnackbarUtils.with(root).setAction("Click") {
                ToastUtils.showShort("Click")
            }.setMessage("测试").show()
        }

        tonalBtn.setOnClickListener {
            Snackbar.make(root, "测试", Snackbar.LENGTH_SHORT).setAction(
                "CLICK"
            ) { ToastUtils.showShort("Click Action") }.show()
        }

        outlineBtn.setOnClickListener {
            jump<CarouseDemoActivity>()
        }
        cardView0.setOnLongClickListener {
            cardView0.isChecked = !cardView0.isChecked
            TransitionManager.beginDelayedTransition(
                root,
//                AutoTransition()
                obtainTransitionSet()
            )//自动记录当前帧为起始Scene，下一步的UI改为结束Scene
            cardView0.layoutParams = cardView0.layoutParams.apply {
                width =
                    if (width == SizeUtils.dp2px(200f)) SizeUtils.dp2px(100f) else SizeUtils.dp2px(
                        200f
                    )
            }

            //修改约束
            /*            val list = mBinding.groupCard.referencedIds.toMutableList().apply { remove(cardView0.id) }
                        mBinding.groupCard.referencedIds =  list.toIntArray()
                        val properties = ConstraintProperties(cardView0)
                        properties.connect(ConstraintProperties.TOP, ConstraintProperties.PARENT_ID, ConstraintProperties.TOP, 0)
                        properties.connect(ConstraintProperties.START, ConstraintProperties.PARENT_ID, ConstraintProperties.START, 0)
                        properties.connect(ConstraintProperties.END, ConstraintProperties.PARENT_ID, ConstraintProperties.END, 0)
                        properties.connect(ConstraintProperties.BOTTOM, ConstraintProperties.PARENT_ID, ConstraintProperties.BOTTOM, 0)
                        properties.apply()*/
            true
        }

        btnToMdDialog.transitionName = "Test"
        btnToMdDialog.setOnClickListener {
            dialogFragment = (dialogFragment ?: DialogDemoFragment().also {
                FragmentUtils.add(
                    supportFragmentManager,
                    it,
                    android.R.id.content,
                    btnToMdDialog
                )
            }).also {
                FragmentUtils.show(it)
            }
        }

        /*        onBackPressedDispatcher.addCallback {
                    FragmentUtils.pop(supportFragmentManager)
                }*/
        cardView1.setOnClickListener {
//            ToastUtils.showShort("Click")
            Toast.makeText(CommonApplication.get(), "Click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainTransitionSet(): TransitionSet {
        return TransitionSet().apply {
            addTransition(ChangeImageTransform())
            addTransition(ChangeBounds())
            addTransition(Fade(Fade.MODE_IN))
        }.also {
//            window.sharedElementEnterTransition = convertToAndroidTransition(it)
        }
    }

    private fun convertToAndroidTransition(transitionSetX: TransitionSet): Transition {
        val transitionSet = android.transition.TransitionSet()
        for (i in 0 until transitionSet.transitionCount) {
            transitionSet.addTransition(transitionSetX.getTransitionAt(i) as Transition)
        }
        return transitionSet
    }

    override fun onBackPressed() {
        dialogFragment?.takeIf { it.isVisible }?.run { FragmentUtils.hide(this) }
            ?: super.onBackPressed()
    }
}