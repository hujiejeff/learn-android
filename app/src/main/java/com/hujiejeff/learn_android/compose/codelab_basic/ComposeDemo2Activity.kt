package com.hujiejeff.learn_android.compose.codelab_basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hujiejeff.learn_android.compose.codelab_basic.FirstCodeLabScreen
import com.hujiejeff.learn_android.compose.ui.theme.CustomizeTheme

class ComposeDemo2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirstCodeLabDemo()
        }
    }
}