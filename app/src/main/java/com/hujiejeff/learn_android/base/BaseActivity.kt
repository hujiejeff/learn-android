package com.hujiejeff.learn_android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hujiejeff.learn_android.util.setActivityContentView

abstract class BaseActivity<V: ViewBinding>: AppCompatActivity() {
    protected lateinit var mBinding: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setActivityContentView<V>(layoutInflater) as V
        mBinding.initView()
    }

    abstract fun V.initView()
}