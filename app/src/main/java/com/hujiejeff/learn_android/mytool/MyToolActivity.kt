package com.hujiejeff.learn_android.mytool

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.hujiejeff.learn_android.base.BaseComposeActivity
import com.hujiejeff.learn_android.compose.ui.theme.AppTheme
import com.hujiejeff.learn_android.mytool.compose.MyToolApp
import com.hujiejeff.learn_android.util.BaseFileUtil
import com.hujiejeff.learn_android.util.ImageUtil
import com.hujiejeff.learn_android.util.ImageUtil.getImagePath
import com.king.zxing.util.BitmapUtils
import com.king.zxing.util.CodeUtils
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyToolActivity : BaseComposeActivity() {
    private val viewModel: MyToolViewModel by viewModels()
    private val PICK_IMAGE = 1;
    private var requestCode = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MyToolApp()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.intentFlow.collect {
                    when (it) {
                        MyToolViewModel.Intent.ScanByCamera -> {
                            requestCode = QRCodeScanActivity.REQUEST_FOR_SCAN
                            startResultActivity.launch(
                                Intent(
                                    this@MyToolActivity,
                                    QRCodeScanActivity::class.java
                                )
                            )
                        }

                        MyToolViewModel.Intent.ScanByAlbum -> {
                            requestCode = PICK_IMAGE
                            startResultActivity.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                                type = "image/*"
                            })
                        }
                        MyToolViewModel.Intent.LoadAPPList -> {
                            viewModel.loadAppInfoList()
                        }
                    }

                }
            }
        }
    }


    private val startResultActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            when (requestCode) {
                QRCodeScanActivity.REQUEST_FOR_SCAN -> {
                    val qrResultCode = QRCodeScanActivity.parseResult(
                        requestCode,
                        it.resultCode,
                        it.data
                    )
                    viewModel.qrScanResult.update {
                        qrResultCode
                    }
                }

                PICK_IMAGE -> {
                    it.data?.data?.also {uri ->
                        val bitmap = ImageUtils.getBitmap(UriUtils.uri2File(uri))
                        val qrResultCode = CodeUtils.parseCode(bitmap) ?: "It seems  no QR code from this image!"
                        viewModel.qrScanResult.update {
                            qrResultCode
                        }
                    }
                }
            }

        }
}