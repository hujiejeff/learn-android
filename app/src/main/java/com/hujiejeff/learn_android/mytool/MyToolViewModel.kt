package com.hujiejeff.learn_android.mytool

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.hujiejeff.learn_android.base.CommonApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyToolViewModel : ViewModel() {
    val qrScanResult = MutableStateFlow<String>("")

    private val intentChannel: Channel<Intent> = Channel(Channel.UNLIMITED)

    val intentFlow = intentChannel.receiveAsFlow()

    val appInfoList = MutableStateFlow<List<APPInfo>>(emptyList())


    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    fun loadAppInfoList() {
        viewModelScope.launch {
            val packageManager = CommonApplication.get().packageManager
            val packageList =
                packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES)
                    .toList()
            withContext(Dispatchers.IO) {
                val infoList = packageList.map {
                    val label = it.applicationInfo.loadLabel(packageManager).toString()
                    val drawable = it.applicationInfo.loadIcon(packageManager)
                    val apkPath = it.applicationInfo.sourceDir
                    AppUtils.isAppSystem()
                    APPInfo(
                        label,
                        it.packageName,
                        drawable,
                        it.versionName,
                        apkPath,
                        FileUtils.getSize(apkPath)
                    )
                }
                appInfoList.update {
                    infoList
                }
            }
        }
    }

    sealed class Intent {
        object ScanByCamera : Intent()

        object ScanByAlbum : Intent()

        object LoadAPPList : Intent()
    }

    enum class APPGenera(val flag: Int = 0) {
        SYSTEM(PackageManager.MATCH_SYSTEM_ONLY), USER(), FREEZE
    }

    data class APPInfo(
        val label: String,
        val packageName: String,
        val drawable: Drawable,
        val versionName: String = "",
        val apkPath: String = "",
        val size: String = ""
    )
}