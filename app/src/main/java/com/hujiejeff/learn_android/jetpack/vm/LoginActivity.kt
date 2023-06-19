package com.hujiejeff.learn_android.jetpack.vm

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
    val viewModel2: LoginViewModel by viewModels()
    val viewModel3: LoginViewModel3 by viewModels({
        defaultViewModelCreationExtras.apply {
        }
    }, {
        defaultViewModelProviderFactory
    })

    override fun ActivityLoginBinding.initView() {
        val viewModel = ViewModelProvider(
            this@LoginActivity,
            ViewModelProvider.NewInstanceFactory.instance
        )[LoginViewModel::class.java]

        viewModel.userName.observe(this@LoginActivity, Observer {

        })
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
            }
        })

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            }
        })

        viewModel3.hello()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsList.collect {
                    Log.d("hujie", "initView: $it")
                }
            }
        }

        mBinding.btnLoadMore.setOnClickListener {
            viewModel.loadNextPage()
        }

    }
}