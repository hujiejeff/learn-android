package com.hujiejeff.learn_android.saf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.getSystemService
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityNewStorageTestBinding
import com.hujiejeff.learn_android.util.BitmapUtil
import java.io.File

class NewStorageTestActivity : BaseActivity<ActivityNewStorageTestBinding>() {
    private val TAG = this.javaClass.simpleName
    private var uriOpenFile: Uri = Uri.parse("/sdcard/sina")
    override fun ActivityNewStorageTestBinding.initView() {
        //Internal Path
        val internalFilePath = filesDir.absolutePath
        val internalCachePath = cacheDir.absolutePath
        //External Path
        //External Private Path
        val externalPrivatePath = getExternalFilesDir("test/test")
        val externalPrivateCachePath = externalCacheDir?.absolutePath
        Log.d(TAG, "internalFilePath:$internalFilePath")
        Log.d(TAG, "internalCachePath:$internalCachePath")
        Log.d(TAG, "externalPrivatePath:$externalPrivatePath")
        Log.d(TAG, "externalPrivateCachePath:$externalPrivateCachePath")

        val s = File(filesDir, "test/222")
        if (!s.exists()) {
            s.mkdirs()
        }
        Log.d(TAG, "externalPrivateCachePath:${File(filesDir, "test/222")}")
        val storageManager = applicationContext.getSystemService<StorageManager>()
        val file = File(externalCacheDir, "test.tmp")
        file.createNewFile()
        getExternalFilesDir("le2/test/test/tea")
        val file2 = File(getExternalFilesDir("le/test/test/tea"), "sss.tmp")
        if (!file2.exists()) {
            file2.createNewFile()
        }

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object : ActivityResultCallback<ActivityResult>{
            override fun onActivityResult(result: ActivityResult) {
                TODO("Not yet implemented")
            }
        })

        btnOpenFile.setOnClickListener {
            openFile(uriOpenFile)
        }
        btnCreateFile.setOnClickListener {
            createFile(Uri.EMPTY)
            /*val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                packageName
            )
            dir.mkdir()
            val destFile = File(dir, "test.tmp")
            destFile.createNewFile()*/
        }
        btnClearExternalCache.setOnClickListener {
            /*val intent = Intent(StorageManager.ACTION_MANAGE_STORAGE)
            launcher.launch(intent)*/
            val bitmap = BitmapUtil.loadBitmapFromView(root)
            BitmapUtil.saveBmpToAlbum(this@NewStorageTestActivity, bitmap, "screenshot.jpg")
        }
        btnClearExternalFile.setOnClickListener {
            val intent = Intent(StorageManager.ACTION_CLEAR_APP_CACHE)
            launcher.launch(intent)
        }
    }


    // Request code for creating a PDF document.
    private val CREATE_FILE = 1

    private fun createFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        startActivityForResult(intent, CREATE_FILE)
    }

    // Request code for selecting a PDF document.
    private val PICK_PDF_FILE = 2

    fun openFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(intent, PICK_PDF_FILE)
    }


    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        resultCode.takeIf { it == Activity.RESULT_OK }?.run {
            when(requestCode)  {
                CREATE_FILE -> {
                    resultData?.data?.also {
                        // Perform operations on the document using its URI.
                        Toast.makeText(this@NewStorageTestActivity, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                PICK_PDF_FILE -> {
                    resultData?.data?.also {
                        Toast.makeText(this@NewStorageTestActivity, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {

                }
            }
        } ?: run {
            Toast.makeText(this@NewStorageTestActivity, "Granted failed", Toast.LENGTH_SHORT).show()
        }

    }
}