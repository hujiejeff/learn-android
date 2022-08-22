package com.hujiejeff.learn_android.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hujiejeff.learn_android.databinding.ActivityConstrainLayoutHomeDemoBinding
import com.hujiejeff.learn_android.util.clickJump

class ConstrainLayoutHomeDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =  ActivityConstrainLayoutHomeDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btn1.clickJump<ConstraintLayoutActivity>()
        binding.btn2.clickJump<FilterImageUseActivity>()
        binding.btn3.clickJump<MotionLayoutUseActivity>()
        binding.btn4.clickJump<SpotifyDemoActivity>()
    }
}