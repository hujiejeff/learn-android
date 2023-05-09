package com.hujiejeff.learn_android.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

object BitmapUtil {
    private const val TAG = "BitmapUtil"
    fun saveBitmapToDCIM(context: Context, bitmap: Bitmap, name: String) {

    }

    fun loadBitmapFromView(v: View): Bitmap {
        var w = v.width
        var h = v.height
        var scale = 1.0f
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        val c = Canvas(bmp)
        c.scale(scale, scale)
        /** 如果不设置canvas画布为白色，则生成透明  */
//        c.drawColor(Color.WHITE);
        v.layout(0, 0, w, h)
        v.draw(c)
        return bmp
    }


    @JvmStatic
    fun saveBmpToAlbum(context: Context, bitmap: Bitmap, name: String) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {
            try {
                val dir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    context.packageName
                )
                if (!dir.exists()) dir.mkdirs()
                val destFile = File(dir, name)
                if (Build.VERSION.SDK_INT < 29) {
                    if (destFile.exists()) destFile.delete()
                    destFile.createNewFile()
                    FileOutputStream(destFile).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                        out.flush()
                    }
                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    intent.data = Uri.parse("file://" + destFile.absolutePath)
                    context.sendBroadcast(intent)
                } else {
                    //android10以上，增加了新字段，自己insert，因为RELATIVE_PATH，DATE_EXPIRES，IS_PENDING是29新增字段
                    val contentValues = ContentValues()
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, destFile.name)
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                    val contentUri: Uri =
                        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        } else {
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        }
                    contentValues.put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_DCIM + "/" + context.packageName
                    )
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
                    val uri: Uri? = context.contentResolver.insert(contentUri, contentValues)
                    if (uri == null) {
                        Log.d("TAG", "Fail")
                        return@Runnable
                    }
                    val resolver = context.contentResolver
                    resolver.openOutputStream(uri)?.use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                        out.flush()
                    }
                    // Everything went well above, publish it!
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    //                            contentValues.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
                    resolver.update(uri, contentValues, null, null)
                }
                Log.i(TAG, "Saved Success")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i(TAG, "Saved Fail")
            }
        })
    }

}