package com.mobiai.app.ui.activity

import SettingFragment
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mobiai.R
import com.mobiai.app.extension.Permission
import com.mobiai.app.ui.fragment.GetStartedFragment
import com.mobiai.app.ui.fragment.MainFragment
import com.mobiai.app.ui.fragment.PermissionFragment
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.activity.BaseActivity
import com.mobiai.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val mainFragment =  MainFragment()
    private val permissionFragment =  PermissionFragment()
    private val getStartedFragment =  GetStartedFragment()
    companion object{
        fun startMain(context: Context, clearTask : Boolean ){
            val intent = Intent(context, MainActivity::class.java).apply {
                if(clearTask){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }

    }
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun createView() {
        attachFragment()
    }

    private fun attachFragment(){
        if(SharedPreferenceUtils.isGetStarted){
            attackFragmentGetStated()
            SharedPreferenceUtils.isGetStarted = false
        }else if(Permission.cameraPermissionWasGiven(this)){
            attachFragmentMain()
        }else{
            attackFragmentPermission()
        }
    }
    private fun attachFragmentMain(){
        supportFragmentManager.beginTransaction().add(R.id.fr_main, mainFragment, MainFragment::class.java.name).show(mainFragment).commit()
    }

    private fun attackFragmentPermission(){
        supportFragmentManager.beginTransaction().add(R.id.fr_main, permissionFragment, PermissionFragment::class.java.name).show(permissionFragment).commit()
    }

    private fun attackFragmentGetStated(){
        supportFragmentManager.beginTransaction().add(R.id.fr_main, getStartedFragment, GetStartedFragment::class.java.name).show(getStartedFragment).commit()
    }

}