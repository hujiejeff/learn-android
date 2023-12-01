package com.hujiejeff.learn_android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.transition.Explode
import androidx.transition.Slide
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.hujiejeff.learn_android.R

inline fun <reified T : Activity> Context.jump() {
    Intent(this, T::class.java).run {
        startActivity(this)
    }
}

inline fun <reified T : Activity> View.clickJump() {
    setOnClickListener {
        context.jump<T>()
    }
}

fun FragmentActivity.startFragmentByShareAnimator(
    fragment: Fragment,
    tag: String,
    sharedElement: View,
    sharedElementName: String
) {
    val transaction = supportFragmentManager.beginTransaction()
    val transform = MaterialContainerTransform(this, true)
    transform.containerColor = MaterialColors.getColor(sharedElement, R.attr.colorSurface)
    transform.fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
    fragment.sharedElementEnterTransition = transform

    val hold = Hold()
    val currentFragment = getCurrentFragment(this)
    hold.addTarget(currentFragment!!.requireView())
    hold.duration = transform.duration
    currentFragment.exitTransition = hold


    transaction.addSharedElement(sharedElement, sharedElementName)
    transaction.replace(R.id.container, fragment, tag)
        .addToBackStack(null)
        .commit()
}

fun getCurrentFragment(activity: FragmentActivity): Fragment? {
    return activity
        .supportFragmentManager
        .findFragmentById(R.id.container)
}


/**
 * saf保存文件
 */
fun getSafSaveFileIntent(
    fileName: String,
    mimeType: String
): Intent {
    return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = mimeType
        putExtra(Intent.EXTRA_TITLE, fileName)
        putExtra(FileProviderUtil.EXTRA_APP_FILE_COPY_NAME, fileName)
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.EMPTY)
    }
}