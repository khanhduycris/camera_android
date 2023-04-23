package com.mobiai.app.ui.dialog

import android.content.Context
import com.mobiai.databinding.DialogEvaluateBinding

class RateDialog(context: Context, callback : (rating : Float) -> Unit) : BaseDialog(context) {

    val binding = DialogEvaluateBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        binding.tvClose.setOnClickListener { dismiss() }

        binding.tvGotoSetting.setOnClickListener {
            callback(binding.ratingbar.rating)
            dismiss()
        }
    }
}