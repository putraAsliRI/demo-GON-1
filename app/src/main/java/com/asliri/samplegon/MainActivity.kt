package com.asliri.samplegon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.asliri.samplegon.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val data = intent?.data
        data?.let {
            val id = it.getQueryParameter("id") // ambil parameter id dari URL
            Toast.makeText(this, "ID Payment: $id", Toast.LENGTH_SHORT).show()
        }

        binding.ivMain.setOnClickListener {
            startActivity(Intent(this, ContactListActivity::class.java))
        }
    }
}
