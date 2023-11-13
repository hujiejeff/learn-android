package com.hujiejeff.learn_android.mytool.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.hujiejeff.learn_android.mytool.compose.codetool.CodeToolScreen

@Composable
fun MyToolApp() {
    val navController = rememberNavController()
    MyToolNavHost()
}

@Composable
fun MyToolNavHost() {
    CodeToolScreen()
}