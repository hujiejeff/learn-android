package com.hujiejeff.learn_android.materia_design.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hujiejeff.learn_android.databinding.ActivityConstrainLayoutHomeDemoBinding
import com.hujiejeff.learn_android.util.clickJump

class ConstrainLayoutHomeDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =  ActivityConstrainLayoutHomeDemoBinding.inflate(layoutInflater)
        binding.run {
            setContentView(this.root)
            setContentView(root)
            btn1.clickJump<ConstraintLayoutActivity>()
            btn2.clickJump<FilterImageUseActivity>()
            btn3.clickJump<MotionLayoutUseActivity>()
            btn4.clickJump<SpotifyDemoActivity>()
            btn5.clickJump<FlowLayoutActivity>()
            btn6.clickJump<MotionCardActivity>()
        }
    }
}