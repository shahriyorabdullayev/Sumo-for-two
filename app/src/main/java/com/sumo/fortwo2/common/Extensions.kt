package com.sumo.fortwo2.common

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import androidx.fragment.app.Fragment
import com.sumo.fortwo2.common.Constants.EN
import com.sumo.fortwo2.common.Constants.LANGUAGE
import com.sumo.fortwo2.common.Constants.MODE
import com.sumo.fortwo2.common.Constants.SOUND
import com.sumo.fortwo2.common.Constants.VIBRATION


fun View.vibrator() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            this.context
                .getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vib = vibratorManager.defaultVibrator
        vib.vibrate(VibrationEffect.createOneShot(100, 1))
    } else {
        val vib = this.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vib.vibrate(70)
    }
}


fun View.setOnVibrationClickListener(vibration: Boolean, listener: () -> Unit) {
    setOnClickListener {
        if (vibration) {
            vibrator()
            listener.invoke()
        } else {
            listener.invoke()
        }
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enabled() {
    isEnabled = true
}
fun View.disabled() {
    isEnabled = false
}

fun Fragment.saveSound(isSound: Boolean) {
    SharedPref(requireContext()).saveBoolean(SOUND, isSound)
}

fun Fragment.getSound(): Boolean {
    return SharedPref(requireContext()).getBoolean(SOUND)
}

fun Fragment.saveVibration(isVibration: Boolean) {
    SharedPref(requireContext()).saveBoolean(VIBRATION, isVibration)
}

fun Fragment.getVibration(): Boolean {
    return SharedPref(requireContext()).getBoolean(VIBRATION)
}

fun Fragment.saveLocale(locale: String) {
    SharedPref(requireContext()).saveString(LANGUAGE, locale)
}

fun Fragment.getLocale(): String {
    return if (SharedPref(requireContext()).getString(LANGUAGE) == null) {
        EN
    } else {
        SharedPref(requireContext()).getString(LANGUAGE)!!
    }
}

fun Fragment.saveMode(mode: Boolean) {
    SharedPref(requireContext()).saveBoolean(MODE, mode)
}

fun Fragment.getMode(): Boolean {
    return SharedPref(requireContext()).getBoolean(MODE)
}
