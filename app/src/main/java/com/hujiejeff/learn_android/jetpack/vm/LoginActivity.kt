package com.hujiejeff.learn_android.jetpack.vm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.Factory
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun ActivityLoginBinding.initView() {
        val viewModel = ViewModelProvider(this@LoginActivity, ViewModelProvider.NewInstanceFactory.instance)[LoginViewModel::class.java]

        viewModel.userName.observe(this@LoginActivity, Observer {

        })
        lifecycle.addObserver(object: DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
            }
        })

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                TODO("Not yet implemented")
            }
        })
    }
}