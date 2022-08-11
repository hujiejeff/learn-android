package com.hujiejeff.musicplayer

import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hujiejeff.musicplayer.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivHook2.setOnClickListener {
            val drawable = binding.ivHook2.drawable
            (drawable as Animatable).start()
        }
    }
}
