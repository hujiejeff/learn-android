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

class Custom2DialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding =
            DialogSampleByDialogfragmentBinding.inflate(requireActivity().layoutInflater)
        val dialog =
            AlertDialog.Builder(context)
                .setView(dialogBinding.root)
                .setCancelable(true)
                .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) //消除白块
        dialog?.window?.attributes?.apply {
            gravity = Gravity.BOTTOM
        }.also {
            dialog?.window?.attributes = it
        }
        return dialog
    }
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val root = inflater.inflate(R.layout.dialog_sample_by_dialogfragment, container, false)
////        dialog?.window?.decorView?.setPadding(0, 0, 0, 0)
//        dialog?.window?.attributes?.apply {
//            gravity = Gravity.BOTTOM
//            width = WindowManager.LayoutParams.MATCH_PARENT
//            height = WindowManager.LayoutParams.MATCH_PARENT
//        }.also {
//
//            dialog?.window?.attributes = it
//            dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//        return root
//    }
}