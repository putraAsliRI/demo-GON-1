package com.asliri.samplegon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asliri.samplegon.databinding.LayoutBottomSheetContactsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class ContactBottomSheet : BottomSheetDialogFragment() {

    private var _binding: LayoutBottomSheetContactsBinding? = null
    private val binding get() = _binding!!   // safe accessor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutBottomSheetContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->

            // Rounded corners
            val shapeAppearance = ShapeAppearanceModel.Builder()
                .setTopLeftCorner(com.google.android.material.shape.CornerFamily.ROUNDED, 32f)
                .setTopRightCorner(com.google.android.material.shape.CornerFamily.ROUNDED, 32f)
                .build()

            val materialShapeDrawable = MaterialShapeDrawable(shapeAppearance)
            materialShapeDrawable.setTint(android.graphics.Color.WHITE)

            sheet.background = materialShapeDrawable

            // langsung expand
            val behavior = BottomSheetBehavior.from(sheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivMain.setOnClickListener {
            startActivity(Intent(requireActivity(), InputNominalActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

