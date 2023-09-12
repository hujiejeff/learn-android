package com.hujiejeff.learn_android.material3

import android.app.AlertDialog
import androidx.transition.TransitionInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.base.BaseFragment
import com.hujiejeff.learn_android.databinding.FragmentDialogDemoBinding

class DialogDemoFragment: BaseFragment<FragmentDialogDemoBinding>() {
    override fun FragmentDialogDemoBinding.initView() {
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)

        btn0.setOnClickListener {
            buildMD3Dialog()
        }
        btn1.setOnClickListener {
            buildMD2Dialog()
        }



    }

    private fun buildMD3Dialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Title")
            .setMessage("Content Message")
            .setNeutralButton("Cancel") { dialog, which ->
                // Respond to neutral button press
            }
            .setNegativeButton("Decline") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Accept") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }

    private fun buildMD2Dialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Title")
            .setMessage("Content")
            .show()
    }
}