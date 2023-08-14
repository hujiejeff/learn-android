package com.hujiejeff.learn_android.base


import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.calligraphy3.R
import io.github.inflationx.viewpump.ViewPump


class CommonApplication : Application() {
     var font = "fonts/fz.ttf"
    private val font_FZ = "fonts/fz.ttf"
    private val fontReboto = "fonts/RobotoCondensed-Regular.ttf"

    companion object {
        private lateinit var application: Application
        fun get() = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        changeFont("")
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
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }

    fun resetSystemFont() {
        changeFont("")
    }
}