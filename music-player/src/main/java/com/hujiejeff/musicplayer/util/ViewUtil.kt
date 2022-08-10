package com.hujiejeff.musicplayer.util

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

fun Context.getDensity() = resources.displayMetrics.density
fun Context.px2dp(px: Int) = (px / getDensity() + 0.5f).toInt()
fun Context.dp2Px(dp: Int) = (dp * getDensity() + 0.5f).toInt()
