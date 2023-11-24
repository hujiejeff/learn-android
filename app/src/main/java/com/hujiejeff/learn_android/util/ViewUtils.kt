package com.hujiejeff.learn_android.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

object ViewUtils {
    fun View.setRadius(px: Float) {
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, px)
            }
        }
    }
}