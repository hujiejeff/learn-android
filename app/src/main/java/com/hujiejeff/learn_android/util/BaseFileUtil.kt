package com.hujiejeff.learn_android.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.hujiejeff.learn_android.base.CommonApplication
import java.io.*


object BaseFileUtil {

    private val context get() = CommonApplication.get()

    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * @param dirName such as "Picture/Bill" -> /Android/data/package-name/files/Pictures/Bill
     */
    fun getAppDir(dirName: String = ""): File {
        return if (isExternalStorageWritable()) {
            context.getExternalFilesDir(dirName) ?: getAppInternalDir(dirName)
        } else {
            getAppInternalDir(dirName)
        }
    }

    fun getAppCacheDir(dirName: String = ""): File {
        return if (isExternalStorageWritable()) {
            context.externalCacheDir?.also { it.checkAndMakeDirs() } ?: getAppInternalDir(dirName)
        } else {
            getAppInternalCacheDir(dirName)
        }
    }

    fun getAppInternalDir(dirName: String = ""): File =
        File(context.filesDir, dirName).also { it.checkAndMakeDirs() }

    fun getAppInternalCacheDir(dirName: String = ""): File =
        File(context.cacheDir, dirName).also { it.checkAndMakeDirs() }

    private fun File.checkAndMakeDirs() {
        if (!exists()) {
            mkdirs()
        }
    }


    fun writeFileFromIS(fos: OutputStream, fis: InputStream): Boolean {
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(fos)
            val data = ByteArray(8192)
            var len: Int
            while (fis.read(data, 0, 8192).also { len = it } != -1) {
                os.write(data, 0, len)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            try {
                fis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



}