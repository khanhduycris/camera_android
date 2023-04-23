package com.mobiai.app.ui.fragment

import PermissionCameraDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.mobiai.app.extension.Permission.cameraPermissionWasGiven
import com.mobiai.app.utils.TrackingEvent
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentPermissionBinding


class PermissionFragment : BaseFragment<FragmentPermissionBinding>() {


    override fun initView() {
        binding.llAccess.setOnClickListener {

            if (!cameraPermissionWasGiven(requireContext())) {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
            TrackingEvent.init(requireContext()).logEvent(TrackingEvent.CLICK_ALLOW_ACCESS_CAMERA)
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPermissionBinding = FragmentPermissionBinding.inflate(inflater, container, false)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            gotoMain()
        } else {
            PermissionCameraDialog(requireContext()){
                gotoSetting()
            }.show()

        }
    }

    override fun onResume() {
        super.onResume()
        if(isGoToSetting && cameraPermissionWasGiven(requireContext())){
            gotoMain()
        }
    }

    private fun gotoMain() {
        val mainFragment = MainFragment()
        replaceFragment(mainFragment, false)
    }

}