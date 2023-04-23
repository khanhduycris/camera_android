package com.mobiai.app.utils

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

object TrackingEvent {

    const val CLICK_BACK_TUTORIAL  = "CLICK_BACK_TUTORIAL"
    const val CLICK_CLOSE_TUTORIAL  = "CLICK_CLOSE_TUTORIAL"
    const val CLICK_NEXT_TUTORIAL  = "CLICK_NEXT_TUTORIAL"
    const val CLICK_ALLOW_ACCESS_CAMERA  = "CLICK_ALLOW_ACCESS_CAMERA"

    var mFirebaseAnalytics: FirebaseAnalytics? = null

    fun init(context: Context): TrackingEvent {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
        return this
    }

    fun logEvent(event: String) {
        mFirebaseAnalytics?.logEvent(event, null)
    }


}