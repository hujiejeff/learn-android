package com.hujiejeff.learn_android.materia_design.constraintlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hujiejeff.learn_android.R

class SpotifyDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_demo)
        val fragment = PlayFragment.newInstance("", "")
        supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
    }
}