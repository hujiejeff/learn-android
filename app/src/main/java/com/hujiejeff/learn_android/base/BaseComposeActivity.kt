package com.hujiejeff.learn_android.base

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

open class BaseComposeActivity: AppCompatActivity() {

    private var onResult: ((Intent?) -> Unit)? = null

    private val startResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK && onResult != null) {
            onResult?.invoke(it.data)
        }
    }

    fun launchActivityForResult(intent: Intent, onResult: (data: Intent?) -> Unit) {
        this.onResult = onResult
        startResultActivity.launch(intent)
    }
}