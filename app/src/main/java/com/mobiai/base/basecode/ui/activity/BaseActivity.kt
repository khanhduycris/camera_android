package com.mobiai.base.basecode.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.mobiai.base.basecode.language.LanguageUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseActivity<V:ViewBinding > : AppCompatActivity(){
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected lateinit var binding : V

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        decorView = window.decorView
        setFullscreen()
        createView()
        LanguageUtil.setupLanguage(this)
    }

    protected abstract fun getLayoutResourceId(): Int

    protected abstract fun getViewBinding() : V

    protected abstract fun createView()

    private var decorView: View? = null
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            decorView!!.systemUiVisibility = hideSystemBars()
        }
    }


    open fun hideSystemBars(): Int {
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    protected open fun setFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            val windowInsetsController = window.insetsController
            if (windowInsetsController != null) {
                windowInsetsController.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = hideSystemBars()
        }
    }
    fun<T> runBackground(action: () -> T, onSuccess: (result: T) -> Unit = {}, onError: (t: Throwable) -> Unit = {}) : Disposable {
        val disposable =  Single.create {
            it.onSuccess(action()!!)
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                onSuccess(it)
            }, {
                onError(it)
            })
        compositeDisposable.add(
            disposable
        )
        return disposable
    }

}

//class MainActivity : BaseActivity<ActivityMainBinding>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.activity_main
//    }
//
//    override fun getViewBinding(): ActivityMainBinding {
//        return ActivityMainBinding.inflate(layoutInflater)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Sử dụng binding để truy cập các view trong layout
//        binding.headerTitle.text = "Hello World!"
//    }
//
//}