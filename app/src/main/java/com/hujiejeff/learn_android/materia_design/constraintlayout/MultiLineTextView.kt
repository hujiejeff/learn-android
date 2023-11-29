package com.hujiejeff.learn_android.materia_design.constraintlayout

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.text.StaticLayout
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.min


class MultiLineTextView(context: Context, attr: AttributeSet? = null, def: Int = 0) :
    AppCompatTextView(context, attr, def) {
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context) : this(context, null, 0)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas) {
        maxLines = min(height / lineHeight, 3)
        super.onDraw(canvas)
/*        StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setEllipsize(TextUtils.TruncateAt.END)
            .setMaxLines(height / lineHeight)
            .setEllipsizedWidth(width - 30)
            .build().draw(canvas)*/
    }
}