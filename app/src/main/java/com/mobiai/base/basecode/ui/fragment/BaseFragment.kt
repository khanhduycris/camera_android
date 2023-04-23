package com.mobiai.base.basecode.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mobiai.base.basecode.ui.activity.BaseActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    open fun handlerBackPressed(){}
    private lateinit var callback: OnBackPressedCallback

    val compositeDisposable = CompositeDisposable()

    companion object{
        var isGoToSetting = false
    }
    protected lateinit var binding: T
    val PERMISSIONS_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handlerBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun initView()

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): T

    fun addAndRemoveCurrentFragment(
        currentFragment: Fragment,
        newFragment: Fragment,
        addToBackStack: Boolean = false
    ) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.remove(currentFragment)
        transaction.add(android.R.id.content, newFragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.add(android.R.id.content, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(android.R.id.content, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    open fun closeFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().remove(fragment).commit()
    }

     fun getRequestPermissionStorageLauncher(
        hasGranted: () -> Unit,
        hasNotAccept: () -> Unit
    ): ActivityResultLauncher<String> {
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                val writeExternalRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if(writeExternalRationale){
                    if (isGranted) {
                        hasGranted()
                    } else {
                        hasNotAccept()
                    }
                }else{
                    hasNotAccept()
                }


            }
        return resultLauncher
    }

    fun gotoSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
        isGoToSetting = true
    }

    fun<T> runBackground(action: () -> T, onSuccess: (result: T) -> Unit = {}, onError: (t: Throwable) -> Unit = {})  {
        (activity as BaseActivity<*>).runBackground(action, onSuccess, onError)
    }

    open fun closeFragment() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }


    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }


}
