package com.mobiai.base.basecode.extensions

import android.util.Log

fun String.logi(TAG: String) {
    Log.i(TAG, this)
}


fun String.loge(TAG: String) {
    Log.e(TAG, this)
}

fun Throwable.loge(TAG: String) {
    Log.e(TAG, this.toString())
}
