package com.hujiejeff.learn_android.mytool

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.FileUtils
import com.hujiejeff.learn_android.base.CommonApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyToolViewModel : ViewModel() {
    val qrScanResult = MutableStateFlow("")

    private val intentChannel: Channel<Intent> = Channel(Channel.UNLIMITED)

    val intentFlow = intentChannel.receiveAsFlow()

    private val viewModelIntentChannel: Channel<ViewModelIntent> = Channel(Channel.UNLIMITED)

    val appInfoList = MutableStateFlow<List<APPInfo>>(emptyList())

    val searchKey = MutableStateFlow("")
    val showAppList = appInfoList.combine(searchKey) {list, searchKey ->
        list.filter {
            if (searchKey.isEmpty()) {
                true
            } else {
                it.label.contains(searchKey)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed(5_000)
    )


    init {
        viewModelScope.launch {
            viewModelIntentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is ViewModelIntent.SearchApp -> {
                        viewModelScope.launch {
                            searchKey.update {
                                intent.searchKey
                            }
                        }
                    }
                }
            }
        }
    }


    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    fun sendIntent(intent: ViewModelIntent) {
        viewModelScope.launch {
            viewModelIntentChannel.send(intent)
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
                    APPInfo(
                        label,
                        it.packageName,
                        drawable.toBitmap().asImageBitmap(),
                        it.versionName ?: "",
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

    sealed class ViewModelIntent() {
        class SearchApp(val searchKey: String) : ViewModelIntent()
    }

    data class APPInfo(
        val label: String,
        val packageName: String,
        val drawable: ImageBitmap,
        val versionName: String = "",
        val apkPath: String = "",
        val size: String = "",
        val isSystemApp: Boolean = false
    )
}