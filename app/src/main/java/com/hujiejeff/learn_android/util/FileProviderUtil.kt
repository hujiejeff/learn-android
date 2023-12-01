package com.hujiejeff.learn_android.util

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import com.hujiejeff.learn_android.BuildConfig
import com.hujiejeff.learn_android.base.CommonApplication
import com.hujiejeff.learn_android.util.ImageUtil.copyFile
import java.io.File
import java.io.FileInputStream

object FileProviderUtil {
    private const val authority = BuildConfig.APPLICATION_ID + ".fileprovider"
    const val APK_PATH = "apks"
    const val EXTRA_APP_FILE_COPY_NAME = BuildConfig.APPLICATION_ID + ".EXTRA_FILE_COPY_NAME"
    //应用目录下文件转成uri
    fun getAppFileUri(file: File): Uri {
        val context = CommonApplication.get() as Context
        return FileProvider.getUriForFile(context, authority, file)
    }

    fun saveFileToUri(sourceFile: File, desUri: Uri): String {
        val os = CommonApplication.get().contentResolver.openOutputStream(desUri)
        os?.use { out ->
            FileInputStream(sourceFile).use { fis ->
                copyFile(fis, out)
            }
        }
        return  com.hujiejeff.learn_android.util.FileUtils.getFile(CommonApplication.get(), desUri).absolutePath
    }
}