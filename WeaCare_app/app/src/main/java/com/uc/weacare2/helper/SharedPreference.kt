package com.uc.weacare2.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPreference (activity: Context) {

    val myPref = "Main_Pref"
    val sharedPreference : SharedPreferences
    val editor : SharedPreferences.Editor

    init {
        sharedPreference = activity.getSharedPreferences(myPref, Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
    }


    fun put(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return sharedPreference.getString(key, null)
    }

    fun clear() {
        editor.clear()
            .apply()
    }

}