package com.hujiejeff.learn_android.constraintlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.hujiejeff.learn_android.databinding.ConstraintlayoutFlowBinding
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

class FlowLayoutActivity: AppCompatActivity() {
    private val sNextGeneratedId: AtomicInteger = AtomicInteger(1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =  ConstraintlayoutFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.run {
            btnAddTagBtn.setOnClickListener {
                val btn = MaterialButton(this@FlowLayoutActivity).apply {
                    cornerRadius = 100
                    id = generateViewId()
                    text = "TAG"
                    width = 200 + ((200) * Math.random()).toInt()
                }
                binding.root.addView(btn)
                val ids = binding.flow.referencedIds
                val idsList = ids.toMutableList().apply {
                    add(btn.id)
                }
                binding.flow.referencedIds =  idsList.toIntArray()
            }
        }
    }

    fun generateViewId(): Int {
        while (true) {
            val result: Int = sNextGeneratedId.get()
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }
}