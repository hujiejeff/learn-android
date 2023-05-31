package com.hujiejeff.learn_android.transition

import android.graphics.Color
import android.view.View
import androidx.transition.*
import com.google.android.material.snackbar.Snackbar
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityTransitionDemoBinding
import com.hujiejeff.learn_android.util.*
import com.luck.picture.lib.utils.ToastUtils
import android.transition.Transition as AndroidTransition

class TransitionDemoActivity : BaseActivity<ActivityTransitionDemoBinding>() {
    override fun ActivityTransitionDemoBinding.initView() {
        btnTrigger.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                container,
//                AutoTransition()
                obtainTransitionSet()
            )//自动记录当前帧为起始Scene，下一步的UI改为结束Scene
            vTarget.visibility = if (vTarget.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        tvSpannableText.text = bold(
            italic("hello"),
            color(Color.RED, "world"),
            italic(color(Color.GREEN, "!")),
            underline("HH"),
            tvSpannableText.click("添加点击事件") {
                ToastUtils.showToast(it.context, "测试点击数")
            }
        )

    }

    private fun obtainTransitionSet(): TransitionSet {
        return TransitionSet().apply {
            addTransition(ChangeImageTransform())
            addTransition(ChangeBounds())
            addTransition(Fade(Fade.MODE_IN))
        }.also {
            window.sharedElementEnterTransition = convertToAndroidTransition(it)
        }
    }


    private fun convertToAndroidTransition(transition: Transition): AndroidTransition {
        return transition as AndroidTransition
    }

    private fun convertToAndroidTransition(transitionSetX: TransitionSet): AndroidTransition {
        val transitionSet = android.transition.TransitionSet()
        for (i in 0 until transitionSet.transitionCount) {
            transitionSet.addTransition(transitionSetX.getTransitionAt(i) as AndroidTransition)
        }
        return transitionSet
    }
}