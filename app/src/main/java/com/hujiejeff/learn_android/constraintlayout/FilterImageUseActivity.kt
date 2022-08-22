package com.hujiejeff.learn_android.constraintlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hujiejeff.learn_android.R
import com.hujiejeff.learn_android.databinding.ConstraintlayoutFilterViewBinding

class FilterImageUseActivity: AppCompatActivity() {
    protected var mCheckId = R.id.radio_button_brightness
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ConstraintlayoutFilterViewBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.slider.addOnChangeListener { rangeSlider, value, fromUser ->
            // Responds to when slider's value is changed
            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.radio_button_brightness -> {
                    binding.ifv.brightness = value
                }
                R.id.radio_button_warmth -> {
                    binding.ifv.warmth = value
                }
                R.id.radio_button_contrast -> {
                    binding.ifv.contrast = value
                }
                R.id.radio_button_saturation -> {
                    binding.ifv.saturation = value
                }
            }
        }



        binding.sliderCrossFade.addOnChangeListener{ rangeSlider, value, fromUser ->
            if (binding.rbOverlay.isChecked) {
                binding.ifv2.crossfade = value
            } else {
                binding.ifv.crossfade = value
            }
        }
    }
}