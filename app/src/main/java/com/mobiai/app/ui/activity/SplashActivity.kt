package com.mobiai.app.ui.activity

import com.mobiai.BuildConfig
import com.mobiai.R
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.activity.BaseSplashActivity
import com.mobiai.databinding.ActivitySplashBinding

class SplashActivity : BaseSplashActivity<ActivitySplashBinding>() {
    override fun getIdAdsSplash(): String = BuildConfig.inter_splash

    override fun getIdNativeLanguage(): String = BuildConfig.native_language


    override fun getLayoutResourceId(): Int  = R.layout.activity_splash

    override fun getViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun createView() {
    }

    override fun openNextScreen() {
        if (SharedPreferenceUtils.languageCode == null) {
            LanguageActivity.start(this, true)
        }  else {
            MainActivity.startMain(this, true)
        }
    }

}