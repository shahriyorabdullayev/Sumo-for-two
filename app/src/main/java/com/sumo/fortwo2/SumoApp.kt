package com.sumo.fortwo2

import android.app.Application
import android.media.MediaFormat.KEY_LANGUAGE
import com.sumo.fortwo2.common.Constants.EN
import com.sumo.fortwo2.common.Constants.RU
import com.sumo.fortwo2.common.SharedPref
import dev.b3nedikt.app_locale.AppLocale
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump
import java.util.*
import kotlin.math.E

class SumoApp: Application() {

    override fun onCreate() {
        super.onCreate()
        ViewPump.init(RewordInterceptor)
        if (SharedPref(this).getString(KEY_LANGUAGE) == EN) {
            AppLocale.desiredLocale = Locale(EN)
        }
    }
}