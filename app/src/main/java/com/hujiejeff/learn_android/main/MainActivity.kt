package com.hujiejeff.learn_android.main

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.WindowInfoTracker
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityMainBinding
import com.hujiejeff.learn_android.util.newInstance
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>() {
    val viewModel: MainViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.S)
    override fun ActivityMainBinding.initView() {
        supportFragmentManager.beginTransaction().replace(R.id.container, newInstance<MainFragment>()).commit()
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                WindowInfoTracker.getOrCreate(this@MainActivity)
                    .windowLayoutInfo(this@MainActivity)
                    .collect { layoutInfo ->
                        // Use newLayoutInfo to update the layout.
                        val foldingFeature = layoutInfo.displayFeatures
                        Log.d("hujie", "onCreate: " + foldingFeature)
                    }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //设置splash
        val sppSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        //设置额外等待时间
        viewModel.initLoading()
        BarUtils.setStatusBarLightMode(this, true)
        ToastUtils.showShort("正在初始化")
        val container = findViewById<View>(R.id.container)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        //第一种方式使用当前的splash
        /*container.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check whether the initial data is ready.
                    return if (viewModel.isReady) {
                        // The content is ready. Start drawing.
                        container.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            }
        )*/

        //使用app的splash
        sppSplash.setKeepOnScreenCondition {
            !viewModel.isReady
        }


        //设置离开动画
        sppSplash.setOnExitAnimationListener { splashScreenViewProvider ->
            // Create your custom animation.
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenViewProvider.view,
                View.TRANSLATION_Y,
                0f,
                -splashScreenViewProvider.view.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd { splashScreenViewProvider.remove() }
            // Run your animation.
            slideUp.start()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}