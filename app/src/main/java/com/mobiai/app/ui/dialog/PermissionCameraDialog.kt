import android.content.Context
import com.mobiai.R
import com.mobiai.app.ui.dialog.BaseDialog
import com.mobiai.databinding.DialogAcceptPermissionBinding
import com.mobiai.databinding.DialogPermisstionCameraBinding

class PermissionCameraDialog(
    context: Context,
    var isGotoSetting: Boolean = false,
    callback: () -> Unit
) : BaseDialog(context, R.style.Theme_Dialog_Allow_Permission) {

    private val binding: DialogPermisstionCameraBinding =
        DialogPermisstionCameraBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        if (isGotoSetting) {
            binding.tvSettting.text = context.getText(R.string.go_to_setting)
        }

        binding.tvSettting.setOnClickListener {
            callback()
            dismiss()
        }

        binding.tvClose.setOnClickListener { dismiss() }
    }

}