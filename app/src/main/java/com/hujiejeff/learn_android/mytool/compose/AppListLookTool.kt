package com.hujiejeff.learn_android.mytool.compose

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ToastUtils
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.mytool.MyToolActivity
import com.hujiejeff.learn_android.mytool.MyToolViewModel
import com.hujiejeff.learn_android.util.BaseFileUtil
import com.hujiejeff.learn_android.util.FileProviderUtil
import com.hujiejeff.learn_android.util.getSafSaveFileIntent
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun APPListScreen(viewModel: MyToolViewModel = viewModel()) {
    val appList by viewModel.appInfoList.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var currentAPPInfo by remember {
        mutableStateOf<MyToolViewModel.APPInfo?>(null)
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }
    if (appList.isEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.sendIntent(MyToolViewModel.Intent.LoadAPPList)
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (appList.isEmpty()) {
            LottieLoading()
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = true
            ) {
                items(items = appList, key = { info -> info.packageName }) {
                    PackageItemInfo(appInfo = it, onClick = {
                        currentAPPInfo = it
                        showBottomSheet = true
                    })
                }
            }
        }
        val scope = rememberCoroutineScope()
        if (showBottomSheet) {
            PackItemClickModal(
                sheetState = sheetState,
                appInfo = currentAPPInfo!!,
                onHide = {
                    showBottomSheet = false
                },
                onShowDetailDialog = {
                    showBottomSheet = false
                    showDetailDialog = true
                })
        }

            if (showDetailDialog) {
                PackageInfoDetailDialog(appInfo = currentAPPInfo!!, onHide = {
                    showDetailDialog = false
                })
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackItemClickModal(
    sheetState: SheetState,
    appInfo: MyToolViewModel.APPInfo,
    onHide: () -> Unit,
    onShowDetailDialog: (MyToolViewModel.APPInfo) -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onHide,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .height(40.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium),
                    bitmap = appInfo.drawable,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = appInfo.label,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        onHide.invoke()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }

            val list = listOf(
                APPClickAction.OpenAPP,
                APPClickAction.UninstallAPP,
                APPClickAction.ShareAPP,
                APPClickAction.OpenAPPDetail,
                APPClickAction.LookAPPInfo,
                APPClickAction.ExtractAPK,
                APPClickAction.ExtractIcon
            )
            LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
                items(list.size) {
                    PackageActionItem(action = list[it]) {
                        if (list[it] == APPClickAction.LookAPPInfo) {
                            onShowDetailDialog.invoke(appInfo)
                        } else {
                            list[it].action.invoke(appInfo)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PackageInfoDetailDialog(
    modifier: Modifier = Modifier,
    appInfo: MyToolViewModel.APPInfo,
    onHide: () -> Unit
) {
    Dialog(
        onDismissRequest = onHide,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)) {
                Text(modifier = Modifier.fillMaxWidth(), text = "应用名:${appInfo.label}")
                Text(modifier = Modifier.fillMaxWidth(), text = "包名:${appInfo.packageName}")
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "启动类:${ActivityUtils.getLauncherActivity(appInfo.packageName)}"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "版本名:${AppUtils.getAppVersionName(appInfo.packageName)}"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "版本号:${AppUtils.getAppVersionCode(appInfo.packageName)}"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "apk目录:${AppUtils.getAppPath(appInfo.packageName)}"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "UID:${AppUtils.getAppUid(appInfo.packageName)}"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "MD5:${AppUtils.getAppSignaturesMD5(appInfo.packageName)}"
                )
            }
        }
    }
}


@Composable
fun PackageActionItem(modifier: Modifier = Modifier, action: APPClickAction, onClick: () -> Unit) {
    Column(modifier = modifier
        .clickable {
            onClick.invoke()
        }
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = action.actionIcon, contentDescription = null)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = action.actionLabel, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun PackageItemInfo(
    modifier: Modifier = Modifier,
    appInfo: MyToolViewModel.APPInfo,
    onClick: (MyToolViewModel.APPInfo) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke(appInfo)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = appInfo.label, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    modifier = Modifier
                        .height(40.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium),
                    bitmap = appInfo.drawable,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = appInfo.versionName, style = MaterialTheme.typography.bodySmall)
                    Text(text = appInfo.size, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

    }
}

@Composable
fun LottieLoading() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        modifier = Modifier.size(80.dp),
        composition = composition,
        progress = { progress },
    )
}

sealed class APPClickAction(
    val actionIcon: ImageVector,
    val actionLabel: String,
    val action: (appInfo: MyToolViewModel.APPInfo) -> Unit
) {
    object OpenAPP : APPClickAction(Icons.Default.OpenInNew, "打开应用", {
        AppUtils.launchApp(it.packageName)
    })

    object UninstallAPP : APPClickAction(Icons.Default.Delete, "卸载应用", {
        AppUtils.uninstallApp(it.packageName)
    })

    object ShareAPP : APPClickAction(Icons.Default.Share, "分享应用", {
        val uri = BaseFileUtil.copyFileToAppDir("apks", "${it.label}.apk", File(it.apkPath))
        Intent().apply {
            action = Intent.ACTION_SEND
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }.run {
            ActivityUtils.startActivity(this)
        }
    })

    object OpenAPPDetail : APPClickAction(Icons.Default.Details, "应用详情", {
        AppUtils.launchAppDetailsSettings(it.packageName)
    })

    object LookAPPInfo : APPClickAction(Icons.Default.Info, "应用信息", {

    })

    object ExtractAPK : APPClickAction(Icons.Default.Upload, "提取安装包", { appInfo ->
        val activity = (ActivityUtils.getTopActivity() as MyToolActivity)
        val fileName = "${appInfo.label}.apk"
        val intent = getSafSaveFileIntent(
            fileName,
            "application/vnd.android.package-archive"
        )
        activity.launchActivityForResult(intent) {
            it?.data?.also { uri ->
                val resultPath = FileProviderUtil.saveFileToUri(File(appInfo.apkPath), uri)
                if (resultPath.isNotEmpty()) {
                    ToastUtils.showShort("保存成功, 文件路径是$resultPath")
                } else {
                    ToastUtils.showShort("保存失败")
                }
            }
        }
    })

    object ExtractIcon : APPClickAction(Icons.Default.Image, "提取图标", {
        ImageUtils.save2Album(it.drawable.asAndroidBitmap(), "MyTool", Bitmap.CompressFormat.PNG)
        ToastUtils.showShort("已保存至相册")
    })
}