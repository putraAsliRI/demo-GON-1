package com.asliri.samplegon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asliri.samplegon.customview.CustomDialog
import com.asliri.samplegon.databinding.ActivityInputNominalBinding
import java.text.NumberFormat
import java.util.Locale

class InputNominalActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityInputNominalBinding.inflate(layoutInflater)
    }
    private var currentAmount: Long = 0L
    private var customDialog: CustomDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set initial display
        updateAmount()

        // Setup keypad
        val buttons = listOf(
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9",
            binding.btn0 to "0",
            binding.btn000 to "000"
        )

        buttons.forEach { (button, value) ->
            button.setOnClickListener {
                appendValue(value)
            }
        }

        // Backspace
        binding.btnBackspace.setOnClickListener {
            if (currentAmount > 0) {
                currentAmount /= 10
                updateAmount()
            }
        }
        initDialog()

        // Konfirmasi
        binding.btnConfirm.setOnClickListener {
            customDialog?.show(supportFragmentManager, "CustomDialog")
        }
    }

    private fun initDialog() {
        customDialog = CustomDialog(
            title = "Gerbang Otentikasi belum terdaftar di nomor telepon ini!",
            message = "Anda perlu mendaftarkan Nomor Telepon pada perangkat ini untuk menghubungkan nya dengan Gerbang Otentikasi Nasional. \\n\n" +
                    "        Silakan lakukan registrasi terlebih dahulu dengan menggunakan data biometrik Anda.\n" +
                    "        \\n \\n Kami akan mengirimkan sms link ke nomor telepon Anda untuk proses registrasi.",
            onConfirm = {

            }
        )
    }

    private fun appendValue(value: String) {
        val newAmountStr = "$currentAmount$value"
        currentAmount = try {
            newAmountStr.toLong()
        } catch (e: NumberFormatException) {
            currentAmount
        }
        updateAmount()
    }

    private fun updateAmount() {
        val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            .format(currentAmount)
        binding.tvAmount.text = formatted
    }
}