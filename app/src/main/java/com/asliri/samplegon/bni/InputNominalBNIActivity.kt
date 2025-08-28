package com.asliri.samplegon.bni

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.asliri.samplegon.customview.RegisterDialog
import com.asliri.samplegon.customview.OtentikasiBottomSheet
import com.asliri.samplegon.data.Resource
import com.asliri.samplegon.data.model.AuthRequest
import com.asliri.samplegon.data.model.TodoViewModel
import com.asliri.samplegon.databinding.ActivityInputNominalBniBinding
import com.asliri.samplegon.pref.PrefManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.Locale
import kotlin.getValue

class InputNominalBNIActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputNominalBniBinding
    private var isUpdating = false
    private var lastValidValue = 10000L // Simpan nilai numerik, bukan string
    private var dialogRegister: RegisterDialog? = null
    private var otentikasiBottomSheet: OtentikasiBottomSheet? = null
    private lateinit var pref: PrefManager
    private val viewModel: TodoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputNominalBniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = PrefManager(this)
        setupEtAmount()
        initDialog()
//        getData()
        observeData()

        binding.btnContinue.setOnClickListener {
            authUser()
        }
    }

    private fun authUser() {
        var authReq = AuthRequest(
            phone = pref.getPhoneNumber()?: "",
            email = pref.getEmail()?: "",
            app_id = "com.asliri.gonbni",
            transaction_ext_id = "ORD-1562792773001",
            transaction_type = "Pembayaran Digital"
        )
        viewModel.authUser(authReq)
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

//    private fun getData() {
//        val appId = intent.getStringExtra("EXTRA_APP_ID") ?: ""
//        val transaction = intent.getStringExtra("EXTRA_TRANSACTION") ?: ""
//
//        if (appId.isNotBlank() && transaction == "register") {
//            setUserRegistered()
//        }else if (appId.isNotBlank() && transaction == "transaction") {
//            Toast.makeText(this, "TRANSACTION SUCCESS", Toast.LENGTH_LONG).show()
//        }
//    }

    private fun openBrowserRegister(url: String = "https://putraasliri.github.io/sample-deeplink/register/index.html") {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun openBrowserTransaction(sessionCode: String ) {
        val url = "https://gon.asliri.id/authentication?session_code="
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url + sessionCode))
        startActivity(intent)
    }

    private fun setUserRegistered() {
        pref.setAlreadyRegister(true)
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
                    app_id = "com.asliri.gonbni"
                )
                viewModel.register(register)
            }
        )
    }

    private fun setupEtAmount() {
        // Set default value
        updateEditText(lastValidValue)

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            private var beforeCursorPosition = 0
            private var beforeLength = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (!isUpdating) {
                    beforeCursorPosition = binding.etAmount.selectionStart
                    beforeLength = s?.length ?: 0
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                val inputText = s.toString()
                val cleanInput = inputText.replace("[^\\d]".toRegex(), "")

                // Jika kosong, set ke 0
                if (cleanInput.isEmpty()) {
                    lastValidValue = 0L
                    updateEditText(0L)
                    return
                }

                try {
                    val newValue = cleanInput.toLong()

                    // Validasi range
                    if (newValue > 999_999_999_999L) {
                        // Jika terlalu besar, kembalikan ke nilai sebelumnya
                        updateEditText(lastValidValue)
                        return
                    }

                    // Deteksi apakah ini operasi penghapusan
                    val currentLength = inputText.length
                    val wasDeleting = currentLength < beforeLength

                    if (wasDeleting && newValue > lastValidValue) {
                        // Jika sedang menghapus tapi nilai jadi lebih besar,
                        // kemungkinan user hapus digit di tengah/awal
                        // Coba potong digit dari belakang
                        val correctedValue = reduceValue(lastValidValue, beforeCursorPosition, beforeLength, currentLength)
                        lastValidValue = correctedValue
                        updateEditText(correctedValue)
                    } else {
                        // Update normal
                        lastValidValue = newValue
                        updateEditText(newValue)
                    }

                } catch (e: Exception) {
                    // Jika error, kembalikan ke nilai sebelumnya
                    updateEditText(lastValidValue)
                }
            }
        })
    }

    private fun reduceValue(originalValue: Long, cursorPos: Int, beforeLen: Int, afterLen: Int): Long {
        val originalStr = originalValue.toString()
        val deletedCount = beforeLen - afterLen + 3 // +3 untuk kompensasi format "Rp" dan ","

        if (deletedCount <= 0 || originalStr.length <= 1) return originalValue

        // Estimasi posisi digit yang dihapus dalam angka asli
        val digitPosition = (cursorPos * originalStr.length.toFloat() / beforeLen).toInt()

        return if (digitPosition <= 0) {
            // Hapus dari depan
            originalStr.drop(1).toLongOrNull() ?: (originalValue / 10)
        } else if (digitPosition >= originalStr.length - 1) {
            // Hapus dari belakang
            originalValue / 10
        } else {
            // Hapus dari tengah
            val newStr = originalStr.removeRange(digitPosition, digitPosition + 1)
            newStr.toLongOrNull() ?: (originalValue / 10)
        }
    }

    private fun updateEditText(value: Long) {
        isUpdating = true

        val formatted = if (value == 0L) {
            ""
        } else {
            formatRupiah(value.toDouble())
        }

        binding.etAmount.setText(formatted)
        binding.etAmount.setSelection(formatted.length)

        isUpdating = false
    }

    private fun formatRupiah(number: Double): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        val formatted = format.format(number)
        // Hapus ,00 di belakang
        return formatted.replace(",00", "")
    }

    private fun showBiometricBottomSheet(sessionCode: String) {
        otentikasiBottomSheet = OtentikasiBottomSheet.newInstance()

        otentikasiBottomSheet?.setOnContinueClickListener {
            // Handle lanjut otentikasi
            openBrowserTransaction(sessionCode)
        }

        otentikasiBottomSheet?.show(supportFragmentManager, "BiometricAuthBottomSheet")
    }

    private fun dismissBiometricBottomSheet() {
        otentikasiBottomSheet?.dismiss()
        otentikasiBottomSheet = null
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}