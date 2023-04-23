package com.mobiai.app.ui.dialog

import android.content.Context
import com.mobiai.R
import com.mobiai.databinding.DialogConfirmDeleteBinding

class DeleteDialog(context: Context, callback : () -> Unit) : BaseDialog (context , R.style.Theme_Dialog_Allow_Permission){

    val binding = DialogConfirmDeleteBinding.inflate(layoutInflater)
    init {
        setContentView(binding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvAllowAccess.setOnClickListener {
            callback()
            dismiss()
        }
    }
}