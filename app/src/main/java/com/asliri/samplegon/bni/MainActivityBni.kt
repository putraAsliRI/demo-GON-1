package com.asliri.samplegon.bni

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.asliri.samplegon.InputDataActivity
import com.asliri.samplegon.databinding.ActivityMainBniBinding
import com.asliri.samplegon.ui.SuccessActivity

class MainActivityBni : ComponentActivity() {

    private val binding by lazy {
        ActivityMainBniBinding.inflate(layoutInflater)
    }
    private var clickCount = 0
    private val CLICK_THRESHOLD = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent?.let {
            val appId = it.getStringExtra("EXTRA_APP_ID")
            val segment = it.getStringExtra("EXTRA_SEGMENT")
            val status = it.getStringExtra("TRANSACTION_STATUS")

            if (segment == "authentication" && status == "SUCCESS") {
                startActivity(Intent(this, SuccessActivity::class.java))
            }else if (segment == "authentication" && status != "SUCCESS") {
                startActivity(Intent(this, InputNominalBNIActivity::class.java))
            }

        }

        binding.ivMain.setOnClickListener {
            startActivity(Intent(this, InputNominalBNIActivity::class.java))
        }

        binding.btnInputData.setOnClickListener {
            clickCount++

            if (clickCount == CLICK_THRESHOLD) {
                onTripleClick()
                clickCount = 0
            }
        }
    }

    private fun onTripleClick() {
        startActivity(Intent(this, InputDataActivity::class.java))
    }
}