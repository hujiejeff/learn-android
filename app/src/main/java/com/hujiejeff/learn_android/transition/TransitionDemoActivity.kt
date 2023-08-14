package com.hujiejeff.learn_android.transition

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import androidx.transition.*
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.base.CommonApplication
import com.hujiejeff.learn_android.databinding.ActivityTransitionDemoBinding
import com.hujiejeff.learn_android.util.*
import com.luck.picture.lib.utils.ToastUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import android.transition.Transition as AndroidTransition

class TransitionDemoActivity : BaseActivity<ActivityTransitionDemoBinding>() {
    override fun ActivityTransitionDemoBinding.initView() {
        btnTrigger.setOnClickListener {
            (CommonApplication.get() as CommonApplication).switchFont()

            TransitionManager.beginDelayedTransition(
                container,
//                AutoTransition()
                obtainTransitionSet()
            )//自动记录当前帧为起始Scene，下一步的UI改为结束Scene
            vTarget.visibility = if (vTarget.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            changeDefaultFont(this@TransitionDemoActivity, root)
            // or 通过 recreate重启当前页面
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


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    fun changeDefaultFont(mContext: Context, rootView: View){
        when(rootView){
            is ViewGroup -> {
                rootView.forEach {
                    changeDefaultFont(mContext,it)
                }
            }
            is TextView -> {
                try {
                    val typeface = Typeface.createFromAsset(mContext.assets, (CommonApplication.get() as CommonApplication).font)
                    val fontStyle = rootView.typeface?.style ?: Typeface.NORMAL
                    rootView.setTypeface(typeface,fontStyle)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}