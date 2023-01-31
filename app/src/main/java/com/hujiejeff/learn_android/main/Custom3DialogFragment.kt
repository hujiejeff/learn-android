package com.hujiejeff.learn_android.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.databinding.DialogSampleBinding
import com.hujiejeff.learn_android.databinding.DialogSampleByDialogfragmentBinding
import kotlinx.coroutines.withTimeoutOrNull

class Custom3DialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_sample_by_dialogfragment3, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) //消除白块
        return dialog
    }
}