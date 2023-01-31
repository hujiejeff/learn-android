package com.hujiejeff.learn_android.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hujiejeff.learn_android.R
import java.lang.IllegalArgumentException

class CustomDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            AlertDialog.Builder(it)
                .setMessage("Start Game?")
                .setCancelable(true)
                .setPositiveButton("Start", { dialog, id -> })
                .setNegativeButton("Cancel", {dialog, id -> })
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}