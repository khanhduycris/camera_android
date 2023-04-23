package com.mobiai.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import com.mobiai.R

abstract class BaseDialog(context: Context, style: Int = R.style.Theme_Dialog) :
    Dialog(context, style) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun show() {
        super.show()
        CheckOpenDialog.isDialogOpen = true
    }

    override fun dismiss() {
        super.dismiss()
        CheckOpenDialog.isDialogOpen = false

    }
}