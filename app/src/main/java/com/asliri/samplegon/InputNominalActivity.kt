package com.asliri.samplegon

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.asliri.samplegon.databinding.ActivityInputNominalBinding
import java.text.NumberFormat
import java.util.Locale

class InputNominalActivity : ComponentActivity() {

    private val binding by lazy {
        ActivityInputNominalBinding.inflate(layoutInflater)
    }
    private var currentAmount: Long = 0L

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

        // Konfirmasi
        binding.btnConfirm.setOnClickListener {
            // TODO: handle konfirmasi, misalnya kirim ke server
        }
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