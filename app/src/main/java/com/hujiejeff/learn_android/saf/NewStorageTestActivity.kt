package com.hujiejeff.learn_android.saf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import com.hujiejeff.learn_android.base.BaseActivity
import com.hujiejeff.learn_android.databinding.ActivityNewStorageTestBinding

class NewStorageTestActivity : BaseActivity<ActivityNewStorageTestBinding>() {
    private val TAG = this.javaClass.simpleName
    private var uriOpenFile: Uri = Uri.parse("/sdcard/sina")
    override fun ActivityNewStorageTestBinding.initView() {
        //Internal Path
        val internalFilePath = filesDir.absolutePath
        val internalCachePath = cacheDir.absolutePath
        //External Path
        //External Private Path
        val externalPrivatePath = getExternalFilesDir("")?.absolutePath
        val externalPrivateCachePath = externalCacheDir?.absolutePath
        Log.d(TAG, "internalFilePath:$internalFilePath")
        Log.d(TAG, "internalCachePath:$internalCachePath")
        Log.d(TAG, "externalPrivatePath:$externalPrivatePath")
        Log.d(TAG, "externalPrivateCachePath:$externalPrivateCachePath")

        btnOpenFile.setOnClickListener {
            openFile(uriOpenFile)
        }
        btnCreateFile.setOnClickListener {
            createFile(Uri.EMPTY)
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