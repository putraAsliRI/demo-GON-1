package com.asliri.samplegon.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asliri.samplegon.databinding.LayoutBottomSheetOtentikasiBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OtentikasiBottomSheet : BottomSheetDialogFragment() {

    private var _binding: LayoutBottomSheetOtentikasiBinding? = null
    private val binding get() = _binding!!

    private var onContinueClicked: (() -> Unit)? = null

    companion object {
        fun newInstance(): OtentikasiBottomSheet {
            return OtentikasiBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutBottomSheetOtentikasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnContinue.setOnClickListener {
            onContinueClicked?.invoke()
            dismiss()
        }
    }

    fun setOnContinueClickListener(listener: () -> Unit) {
        onContinueClicked = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}