package com.hujiejeff.learn_android.mytool.compose.codetool

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.hujiejeff.learn_android.mytool.MyToolViewModel
import com.hujiejeff.learn_android.mytool.QRCodeScanActivity
import com.hujiejeff.learn_android.mytool.compose.TabButtonGroup
import com.king.zxing.util.CodeUtils
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.UriUtils
import com.hujiejeff.learn_android.util.ImageUtil
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScreen(modifier: Modifier = Modifier, viewModel: MyToolViewModel = viewModel()) {
    var textInput by remember { mutableStateOf("") }
    val tabs = listOf(QR.SCAN, QR.GENERATE)
    var currentAction by remember {
        mutableStateOf(tabs[0])
    }
    var bitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    val activity = LocalContext.current.takeIf { it is Activity }?.let { it as Activity }
    Column(modifier.padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            val activity = LocalContext.current.takeIf { it is Activity }?.let { it as Activity }
            when (currentAction) {
                QR.SCAN -> {
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            viewModel.sendIntent(MyToolViewModel.Intent.ScanByCamera())
                        }) {
                        Icon(imageVector = Icons.Default.Camera, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Scan from camera")
                    }
                    Spacer(modifier = modifier.height(8.dp * 2))
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            viewModel.sendIntent(MyToolViewModel.Intent.ScanByAlbum())
                        }) {
                        Icon(imageVector = Icons.Default.Image, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Scan from Image")
                    }
                }

                QR.GENERATE -> {
                    OutlinedTextField(
                        modifier = modifier.fillMaxSize(),
                        maxLines = 5,
                        value = textInput,
                        onValueChange = {
                            textInput = it
                        })
                }
            }
        }
        Spacer(modifier = modifier.height(8.dp * 2))
        TabButtonGroup(
            modifier = Modifier,
            defaultIndex = 0,
            size = tabs.size,
            getLabel = { index -> tabs[index].name },
            onSelectedListener = { index ->
                currentAction = tabs[index]
            })
        val result by viewModel.qrScanResult.collectAsState()
        when (currentAction) {
            QR.SCAN -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = result,
                    style = MaterialTheme.typography.bodySmall
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        ClipboardUtils.copyText(result)
                        ToastUtils.showShort("Copied")
                    }) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
                }
            }

            QR.GENERATE -> {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    IconButton(onClick = {
                        bitmap?.also {
                            ImageUtil.copyBitmap2Clipboard(it.asAndroidBitmap())
                            ToastUtils.showShort("Already Copied on Clipboard!")
                        }
                    }) {
                        Icon(imageVector = Icons.Default.FileCopy, contentDescription = null)
                    }

                    IconButton(onClick = {
                        bitmap?.also {
                            ImageUtils.save2Album(bitmap?.asAndroidBitmap(), Bitmap.CompressFormat.JPEG)
                            ToastUtils.showShort("Save Success!")
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    }
                }
                val height = 200.dp
                textInput.takeIf { it.isNotEmpty() }?.run {
                    val px = with(LocalDensity.current) { height.toPx() }
                    bitmap = CodeUtils.createQRCode(
                        this,
                        px.toInt(),
                        MaterialTheme.colorScheme.primary.toArgb()
                    ).asImageBitmap()
                    bitmap?.also {
                        Image(
                            modifier = Modifier
                                .size(height)
                                .align(Alignment.CenterHorizontally),
                            painter = BitmapPainter(it),
                            contentDescription = textInput
                        )
                    }
                }

            }
        }
    }

}

enum class QR(name: String) {
    SCAN("Scan"), GENERATE("Generate")
}