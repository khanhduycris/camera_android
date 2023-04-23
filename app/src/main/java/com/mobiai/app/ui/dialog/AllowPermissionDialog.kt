package com.mobiai.app.ui.dialog

import android.content.Context
import com.mobiai.R
import com.mobiai.databinding.DialogAcceptPermissionBinding

class AllowPermissionDialog(
    context: Context,
    var isGotoSetting: Boolean = false,
    callback: () -> Unit
) : BaseDialog(context, R.style.Theme_Dialog_Allow_Permission) {

    private val binding: DialogAcceptPermissionBinding =
        DialogAcceptPermissionBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        if (isGotoSetting) {
            binding.tvAllowAccess.text = context.getText(R.string.go_to_setting)
        }

        binding.tvAllowAccess.setOnClickListener {
            callback()
            dismiss()
        }

        binding.tvCancel.setOnClickListener { dismiss() }
    }

}