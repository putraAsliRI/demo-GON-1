package com.asliri.samplegon

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.asliri.samplegon.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashGojekActivity : ComponentActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    private var isDeepLinkHandled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // cek deeplink
        handleDeepLink(intent)

        // kalau tidak ada deeplink → lanjut normal splash
        if (!isDeepLinkHandled) {
            lifecycleScope.launch {
                delay(3000)
                startActivity(Intent(this@SplashGojekActivity, MainActivityGojek::class.java))
                finish()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.let { it ->
            val segments = it.getStringExtra("EXTRA_SEGMENT")
            val directionPath = it.getStringExtra("TRANSACTION_STATUS")
            //REGISTER
            if ( segments == "register") {
                val intentRegister = Intent(this, InputNominalActivity::class.java).apply {
                    putExtra("EXTRA_SEGMENT", segments)
                }
                startActivity(intentRegister)
                finish()
            }

            //TRANSACTION
            if (segments == "authentication") {
                val intentRegister = Intent(this, MainActivityGojek::class.java).apply {
                    putExtra("EXTRA_SEGMENT", segments)
                    putExtra("TRANSACTION_STATUS", directionPath)
                }

                startActivity(intentRegister)
                finish()
            }

        }
    }
}
