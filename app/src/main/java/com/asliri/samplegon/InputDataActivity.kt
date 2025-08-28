package com.asliri.samplegon

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.asliri.samplegon.databinding.ActivityInputDataBinding
import com.asliri.samplegon.pref.PrefManager

class InputDataActivity: ComponentActivity() {

    private val binding by lazy {
        ActivityInputDataBinding.inflate(layoutInflater)
    }
    private lateinit var pref: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        pref = PrefManager(this)
        setupPhoneFormatter()
        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            etEmail.setText(pref.getEmail())
            etPhone.setText(pref.getPhoneNumber())

            btnSave.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val phone = etPhone.text.toString().trim()

                if (email.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(this@InputDataActivity, "Email dan No HP harus diisi", Toast.LENGTH_SHORT).show()
                } else {
                    pref.setEmail(email)
                    pref.setPhoneNumber(phone)
                    Toast.makeText(this@InputDataActivity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setupPhoneFormatter() {
        binding.etPhone.addTextChangedListener(object : TextWatcher {
            private var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isEditing) return
                isEditing = true

                s?.let {
                    var phone = it.toString()

                    // Hapus semua karakter selain angka
                    phone = phone.replace("[^0-9]".toRegex(), "")

                    // Ganti 0 di depan menjadi 62
                    if (phone.startsWith("0")) {
                        phone = "62" + phone.substring(1)
                    }

                    it.replace(0, it.length, phone)
                }

                isEditing = false
            }
        })
    }
}