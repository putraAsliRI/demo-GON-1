package com.asliri.samplegon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import com.asliri.samplegon.bni.InputNominalBNIActivity
import com.asliri.samplegon.databinding.ActivityFirstBinding
import com.asliri.samplegon.databinding.ActivitySplashBinding
import com.asliri.samplegon.databinding.ActivitySplashBniBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {

    private val binding by lazy { ActivityFirstBinding.inflate(layoutInflater) }

    private var isDeepLinkHandled = false // ✅ penanda deeplink sudah diproses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.btnBni.setOnClickListener {
              startActivity(Intent(this@SplashActivity, SplashBniActivity::class.java))
        }
        binding.btnGojek.setOnClickListener {
            startActivity(Intent(this@SplashActivity, SplashGojekActivity::class.java))
        }
        binding.btnBni.visibility = View.GONE
        binding.btnGojek.visibility = View.GONE

        // cek deeplink
        handleDeepLink(intent)

        // kalau tidak ada deeplink → lanjut normal splash
        if (!isDeepLinkHandled) {
            lifecycleScope.launch {
                delay(3000)
                binding.btnBni.visibility = View.VISIBLE
                binding.btnGojek.visibility = View.VISIBLE
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        data?.let { uri ->
            val app = uri.getQueryParameter("app") ?: ""
            val sessionCode = uri.getQueryParameter("session_code") ?: ""
            val directionPath = uri.getQueryParameter("direction_path") ?: ""
            val segments = uri.pathSegments

            //REGISTER
            if (app == "bni" && segments[0] == "register") {
                val intentRegister = Intent(this, SplashBniActivity::class.java).apply {
                    putExtra("EXTRA_APP_ID", app)
                    putExtra("EXTRA_SEGMENT", segments[0])
                }
                startActivity(intentRegister)
                finish()
            }
            if (app == "gojek" && segments[0] == "register") {
                val intentRegister = Intent(this, SplashGojekActivity::class.java).apply {
                    putExtra("EXTRA_APP_ID", app)
                    putExtra("EXTRA_SEGMENT", segments[0])
                }
                startActivity(intentRegister)
                finish()
            }

            //TRANSACTION
            if (app == "bni" && segments[0] == "authentication") {
                val intentRegister = Intent(this, SplashBniActivity::class.java).apply {
                    putExtra("EXTRA_APP_ID", app)
                    putExtra("EXTRA_SEGMENT", segments[0])
                    putExtra("TRANSACTION_STATUS", directionPath)
                }

                startActivity(intentRegister)
                finish()
            }
            if (app == "gojek" && segments[0] == "authentication") {
                val intentRegister = Intent(this, SplashGojekActivity::class.java).apply {
                    putExtra("EXTRA_APP_ID", app)
                    putExtra("EXTRA_SEGMENT", segments[0])
                    putExtra("TRANSACTION_STATUS", directionPath)
                }

                startActivity(intentRegister)
                finish()
            }

        }
    }
}
