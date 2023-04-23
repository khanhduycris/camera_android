package com.mobiai.base.basecode.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApNativeAd
import com.ads.control.billing.AppPurchase
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.base.basecode.storage.AdsRemote
import com.mobiai.base.basecode.storage.SharedPreferenceUtils

abstract class BaseSplashActivity<V : ViewBinding> : BaseActivity<V>(){
    var isOnStop = false
    private var showNextScreenHandler = Handler(Looper.getMainLooper())

    private var screenLock: PowerManager.WakeLock? = null
    abstract fun getIdAdsSplash(): String

    abstract fun getIdNativeLanguage() : String

    abstract fun openNextScreen()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wakeUpScreen()
        initAdsNativeLanguage()
        //TODO get remote config
        setUpPurchase()
    }

    @SuppressLint("InvalidWakeLockTag")
    protected fun wakeUpScreen() {
        screenLock = (getSystemService(POWER_SERVICE) as PowerManager).newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "mywakelocktag")
        screenLock?.acquire(3 * 60 * 1000L)
    }

    private fun initAdsNativeLanguage() {
        Log.d("TAGGG","-------++++++> purchse : ${AppPurchase.getInstance().isPurchased} :first:  ${SharedPreferenceUtils.firstOpenApp} , value: ${App.getStorageCommon()?.nativeAdLanguage?.value}, ${AdsRemote.showNativeLanguage}")
        if (!AppPurchase.getInstance().isPurchased
            && SharedPreferenceUtils.firstOpenApp
            && App.getStorageCommon()?.nativeAdLanguage?.value == null
            && AdsRemote.showNativeLanguage) {

            Log.d("TAGGG","-------> ${AppPurchase.getInstance().isPurchased} : ${SharedPreferenceUtils.firstOpenApp} , value: ${App.getStorageCommon()?.nativeAdLanguage?.value}, ${AdsRemote.showNativeLanguage}")
            AperoAd.getInstance().loadNativeAdResultCallback(
                this,
                getIdNativeLanguage(),
                R.layout.native_admod_language,
                object : AperoAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        App.getStorageCommon()?.nativeAdLanguage?.postValue(nativeAd)
                        Log.d("TAGGG", "onNativeAdLoaded ---------------> $nativeAd")
                    }

                    override fun onAdFailedToLoad(adError: ApAdError?) {
                        super.onAdFailedToLoad(adError)
                        App.getStorageCommon()?.nativeAdLanguage?.postValue(null)
                        Log.d("TAGGG", "onAdFailedToLoad ---------------> null")

                    }
                }
            )
        }
    }

    override fun onStop() {
        super.onStop()
        isOnStop = true
    }

    override fun onStart() {
        super.onStart()
        isOnStop = false
    }

    override fun onDestroy() {
        super.onDestroy()
        showNextScreenHandler.removeCallbacksAndMessages(null)
    }

    private fun setUpPurchase(){
        if (AppPurchase.getInstance().initBillingFinish) {
            showNextScreen()
        } else {
            AppPurchase.getInstance().setBillingListener({ i: Int ->
                runOnUiThread {
                    showNextScreen()
                }
            }, 2000)
        }
    }

     private fun showNextScreen() {
        if (AppPurchase.getInstance().isPurchased(this)) {
            showNextScreenHandler.postDelayed({
                openNextScreen()
            }, 2000)
        } else {
            startNotPurchase()
        }
    }

    private fun startNotPurchase(){
        if(AdsRemote.showAdsSplash){
            AperoAd.getInstance().setInitCallback {
                AperoAd.getInstance()
                    .loadSplashInterstitialAds(
                        this,
                        getIdAdsSplash(),
                        20000,
                        2000,
                        true,
                        adAperoCallBack
                    )
            }
        }else{
            openNextScreen()
        }
    }
    private var adAperoCallBack = object : AperoAdCallback() {
        override fun onAdLoaded() {
            super.onAdLoaded()
        }

        override fun onAdClosed() {
            super.onAdClosed()
            if (isDestroyed || isFinishing || isOnStop) return
            openNextScreen();
            Log.d("TAGGG","----------------> onAdClosed ")
        }

        override fun onAdFailedToLoad(adError: ApAdError?) {
            super.onAdFailedToLoad(adError)
            if (isDestroyed || isFinishing || isOnStop) return
            openNextScreen();
            Log.d("TAGGG","----------------> onAdClosed ")

        }

        override fun onAdFailedToShow(adError: ApAdError?) {
            super.onAdFailedToShow(adError)
            if (isDestroyed || isFinishing || isOnStop) return
            openNextScreen();
            Log.d("TAGGG","----------------> onAdFailedToLoad ")

        }

        override fun onNextAction() {
            super.onNextAction()
            if (isDestroyed || isFinishing || isOnStop) return
            openNextScreen();
            Log.d("TAGGG","----------------> onNextAction ")

        }
    }

}