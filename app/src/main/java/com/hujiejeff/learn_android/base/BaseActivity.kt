package com.hujiejeff.learn_android.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import androidx.window.layout.WindowInfoTracker
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.util.setActivityContentView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseActivity<V: ViewBinding>: AppCompatActivity() {
    protected lateinit var mBinding: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (CommonApplication.get().isMaterial3Theme()) {
            setTheme(R.style.My_Material3)
        }
        mBinding = setActivityContentView<V>(layoutInflater) as V
        mBinding.initView()
    }

    abstract fun V.initView()
}