package com.sumo.fortwo2.common

import android.content.Context

class SharedPref(private val context: Context) {

    private val pref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return pref.getString(key, null)
    }

    fun saveBoolean(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }


}