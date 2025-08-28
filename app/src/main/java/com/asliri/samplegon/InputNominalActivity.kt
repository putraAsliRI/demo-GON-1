package com.asliri.samplegon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.asliri.samplegon.customview.RegisterDialog
import com.asliri.samplegon.customview.OtentikasiBottomSheet
import com.asliri.samplegon.data.Resource
import com.asliri.samplegon.data.model.AuthRequest
import com.asliri.samplegon.data.model.TodoViewModel
import com.asliri.samplegon.databinding.ActivityInputNominalBinding
import com.asliri.samplegon.pref.PrefManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.Locale
import kotlin.getValue

class InputNominalActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityInputNominalBinding.inflate(layoutInflater)
    }
    private var currentAmount: Long = 0L
    private var registerDialog: RegisterDialog? = null
    private var otentikasiBottomSheet: OtentikasiBottomSheet? = null
    private var isUpdating = false
    private var lastValidValue = 10000L // Simpan nilai numerik, bukan string
    private var dialogRegister: RegisterDialog? = null
    private lateinit var pref: PrefManager
    private val viewModel: TodoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        pref = PrefManager(this)

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
            authUser()
        }
        observeData()
    }

    private fun authUser() {
        var authReq = AuthRequest(
            phone = pref.getPhoneNumber()?: "",
            email = pref.getEmail()?: "",
            app_id = "com.asliri.gongojek",
            transaction_ext_id = "ORD-1562792773001",
            transaction_type = "Pembayaran Digital"
        )
        viewModel.authUser(authReq)
    }

    private fun initDialog() {
        dialogRegister = RegisterDialog(
            title = "Otentikasi belum terdaftar di nomor telepon ini!",
            message = "Anda perlu mendaftarkan Nomor Telepon pada perangkat ini untuk menghubungkan nya dengan Gerbang Otentikasi Nasional.\n" +
                    "Silakan lakukan registrasi terlebih dahulu dengan menggunakan data biometrik Anda.\n" +
                    "\nKami akan mengirimkan sms link ke nomor telepon Anda untuk proses registrasi.",
            onConfirm = {
                var register = AuthRequest(
                    phone = pref.getPhoneNumber()?: "",
                    email = pref.getEmail()?: "",
                    app_id = "com.asliri.gongojek"
                )
                viewModel.register(register)
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

    private fun showBiometricBottomSheet(sessionCode: String) {
        otentikasiBottomSheet = OtentikasiBottomSheet.newInstance()

        otentikasiBottomSheet?.setOnContinueClickListener {
            // Handle lanjut otentikasi
            openBrowserTransaction(sessionCode)
        }

        otentikasiBottomSheet?.show(supportFragmentManager, "BiometricAuthBottomSheet")
    }

    private fun openBrowserTransaction(sessionCode: String ) {
        val url = "https://gon.asliri.id/authentication?session_code="
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url + sessionCode))
        startActivity(intent)
    }

    private fun dismissBiometricBottomSheet() {
        otentikasiBottomSheet?.dismiss()
        otentikasiBottomSheet = null
    }

    private fun observeData() {
        viewModel.authUser.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    if (state.data.result.sessionCode.isNotBlank()) {
                        showBiometricBottomSheet(state.data.result.sessionCode)
                    }
                    showLoading(false)
                }
                is Resource.Error -> {
                    when (state.code) {
                        401 -> {}
                        403 -> {}
                        else -> {
                            dialogRegister?.show(supportFragmentManager, "CustomDialog")
                        }
                    }
                    showLoading(false)
                }
            }
        }

        viewModel.register.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                }
                is Resource.Error -> {
                    when (state.code) {
                        401 -> {}
                        403 -> {}
                        else -> {

                        }
                    }
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

}