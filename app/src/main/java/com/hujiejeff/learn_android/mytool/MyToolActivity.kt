package com.hujiejeff.learn_android.mytool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hujiejeff.learn_android.compose.ui.theme.AppTheme
import com.hujiejeff.learn_android.mytool.compose.MyToolApp

class MyToolActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MyToolApp()
            }
        }
    }
}