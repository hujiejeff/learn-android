package com.hujiejeff.learn_android.util

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.text.toSpannable

/**
 * Returns a CharSequence that concatenates the specified array of CharSequence
 * objects and then applies a list of zero or more tags to the entire range.
 *
 * @param content an array of character sequences to apply a style to
 * @param tags the styled span objects to apply to the content
 *        such as android.text.style.StyleSpan
 */
private fun apply(content: Array<out CharSequence>, vararg tags: Any): CharSequence {
    return SpannableStringBuilder().apply {
        openTags(tags)
        content.forEach { charSequence ->
            append(charSequence)
        }
        closeTags(tags)
    }
}

/**
 * Iterates over an array of tags and applies them to the beginning of the specified
 * Spannable object so that future text appended to the text will have the styling
 * applied to it. Do not call this method directly.
 */
private fun Spannable.openTags(tags: Array<out Any>) {
    tags.forEach { tag ->
        setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK)
    }
}

/**
 * "Closes" the specified tags on a Spannable by updating the spans to be
 * endpoint-exclusive so that future text appended to the end will not take
 * on the same styling. Do not call this method directly.
 */
private fun Spannable.closeTags(tags: Array<out Any>) {
    tags.forEach { tag ->
        if (length > 0) {
            setSpan(tag, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            removeSpan(tag)
        }
    }
}


/**
 * Returns a CharSequence that applies boldface to the concatenation
 * of the specified CharSequence objects.
 */
fun bold(vararg content: CharSequence): CharSequence = apply(content, StyleSpan(Typeface.BOLD))

/**
 * Returns a CharSequence that applies italics to the concatenation
 * of the specified CharSequence objects.
 */
fun italic(vararg content: CharSequence): CharSequence = apply(content, StyleSpan(Typeface.ITALIC))

/**
 * Returns a CharSequence that applies a foreground color to the
 * concatenation of the specified CharSequence objects.
 */
fun color(color: Int, vararg content: CharSequence): CharSequence =
    apply(content, ForegroundColorSpan(color))

fun underline(vararg content: CharSequence): CharSequence = apply(content, UnderlineSpan())

fun TextView.click(vararg content: CharSequence, clicker: (View) -> Unit): CharSequence =
    apply(content, object : ClickableSpan() {
        override fun onClick(widget: View) {
            clicker.invoke(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = currentTextColor
            ds.isUnderlineText = false
        }
    }).also {
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }
