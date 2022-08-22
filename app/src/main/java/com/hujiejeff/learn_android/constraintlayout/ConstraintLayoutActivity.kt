package com.hujiejeff.learn_android.constraintlayout

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.databinding.ConstraintlayoutAnimatorBeforeBinding

class ConstraintLayoutActivity : AppCompatActivity() {
    private lateinit var binding: ConstraintlayoutAnimatorBeforeBinding
    private lateinit var constraintSetStart: ConstraintSet
    private lateinit var constraintReset: ConstraintSet
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConstraintlayoutAnimatorBeforeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        constraintReset = ConstraintSet()
        constraintSetStart = ConstraintSet()
        constraintReset.clone(binding.ctl)
        binding.btn.setOnClickListener {
            if (isOpen) {
                close()
            } else {
                open()
            }
            isOpen = !isOpen
        }
    }

    fun open() {
        TransitionManager.beginDelayedTransition(binding.ctl)
        constraintSetStart.load(this, R.layout.constraintlayout_animator)
        constraintSetStart.applyTo(binding.ctl)
    }

    fun open2() {
        TransitionManager.beginDelayedTransition(binding.ctl)
        constraintSetStart.clone(binding.ctl)
        constraintSetStart.constrainCircle(R.id.btn1, R.id.btn, 75.dp, 0f) //0
        constraintSetStart.constrainCircle(R.id.btn2, R.id.btn, 75.dp, 315f) // 315
        constraintSetStart.constrainCircle(R.id.btn3, R.id.btn, 75.dp, 270f) // 270
        constraintSetStart.applyTo(binding.ctl)
    }

    fun open3() {
/*        val properties = ConstraintProperties(binding.btn1)
        properties.translationZ(32f).c
            .margin(ConstraintSet.START, 43)
            .apply()*/
    }

    private fun close() {
        TransitionManager.beginDelayedTransition(binding.ctl)
        constraintReset.applyTo(binding.ctl)
    }

    private inline val Int.dp: Int get() = this.dp2px()

    fun Number.dp2px(): Int {
        val f = toFloat()
        val scale: Float = Resources.getSystem().displayMetrics.density
        return (f * scale + 0.5f).toInt()
    }

}