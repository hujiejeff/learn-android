package com.hujiejeff.learn_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hujiejeff.learn_android.databinding.ActivityMainBinding
import com.hujiejeff.learn_android.main.MainFragment
import com.hujiejeff.learn_android.util.newInstance

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.container, newInstance<MainFragment>()).commit()
    }
}