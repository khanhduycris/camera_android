package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentGetStartedBinding

class GetStartedFragment : BaseFragment<FragmentGetStartedBinding>() {

    private val permissionFragment = PermissionFragment()
    override fun initView() {
        binding.tvGetStated.setOnClickListener {
            addFragment(permissionFragment)
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGetStartedBinding {
        return FragmentGetStartedBinding.inflate(inflater, container, false)
    }
}