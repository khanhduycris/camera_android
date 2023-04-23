package com.mobiai.base.basecode.storage

import com.mobiai.app.App

object SharedPreferenceUtils {
    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    private const val LANGUAGE = "LANGUAGE"
    private const val GET_STATED = "GET_STATED"
    private const val IS_SHOWED_TUTORIAL = "IS_SHOWED_TUTORIAL"
    private const val PATH_IMAGE_LATEST = "PATH_IMAGE_LATEST"

    var firstOpenApp: Boolean
        get() = App.instanceSharePreference.getValueBool(FIRST_OPEN_APP, true)
        set(value) = App.instanceSharePreference.setValueBool(FIRST_OPEN_APP, value)

    var languageCode: String?
        get() = App.instanceSharePreference.getValue(LANGUAGE, null)
        set(value) = App.instanceSharePreference.setValue(LANGUAGE, value)

    var isGetStarted : Boolean
        get() = App.instanceSharePreference.getValueBool(GET_STATED, true)
        set(value) = App.instanceSharePreference.setValueBool(GET_STATED, value)

    var isShowedTutorialFistOpen : Boolean
        get() = App.instanceSharePreference.getValueBool(IS_SHOWED_TUTORIAL, true)
        set(value) = App.instanceSharePreference.setValueBool(IS_SHOWED_TUTORIAL, value)


    var pathImageLatest : String?
        get() = App.instanceSharePreference.getValue(PATH_IMAGE_LATEST, null)
        set(value) = App.instanceSharePreference.setValue(PATH_IMAGE_LATEST, value)

}