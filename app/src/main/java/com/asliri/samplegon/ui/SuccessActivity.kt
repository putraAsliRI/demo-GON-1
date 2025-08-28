package com.asliri.samplegon.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.asliri.samplegon.databinding.ActivitySuccessBinding

class SuccessActivity: ComponentActivity() {

    private val binding by lazy { ActivitySuccessBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}