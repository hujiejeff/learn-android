package com.hujiejeff.learn_android.base


import android.app.Application
import coil.ImageLoaderFactory
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.hujiejeff.learn_android.compose.ColiLoaderFactoryImpl
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.calligraphy3.R as FontR
import io.github.inflationx.viewpump.ViewPump


class CommonApplication : Application(), ImageLoaderFactory by ColiLoaderFactoryImpl()  {
     var font = "fonts/fz.ttf"
    private val font_FZ = "fonts/fz.ttf"
    private val fontReboto = "fonts/RobotoCondensed-Regular.ttf"

    companion object {
        private lateinit var application: CommonApplication
        fun get() = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        changeFont()
        Utils.init(this)
    }

    fun switchFont() {
        font = if (font == font_FZ) {
            fontReboto
        } else {
            font_FZ
        }
        changeFont(font)
    }

    fun changeFont(f: String = "") {

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(f)
                            .setFontAttrId(FontR.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }

    fun resetSystemFont() {
        changeFont("")
    }

    fun setMaterial3Theme(useMaterial3: Boolean) {
        SPUtils.getInstance().put("user_material3", useMaterial3)
    }

    fun isMaterial3Theme(): Boolean = SPUtils.getInstance().getBoolean("user_material3", false)
}