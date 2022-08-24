package com.hujiejeff.learn_android.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.databinding.ActivityMotionLayoutUseBinding

class MotionLayoutUseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMotionLayoutUseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMotionLayoutUseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.root.postDelayed(::start, 2000)
        start()
    }

    fun start() {

        binding.motionLayout.transitionToEnd()
    }
}