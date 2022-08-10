package com.hujiejeff.musicplayer.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hujiejeff.musicplayer.ViewModelFactory

public inline fun AppCompatActivity.transaction(action: FragmentTransaction.() -> Unit) {
    supportFragmentManager.beginTransaction().run {
        action()
        commit()
    }
}
 fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application))[viewModelClass]

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(activity!!.application)).get(viewModelClass)
