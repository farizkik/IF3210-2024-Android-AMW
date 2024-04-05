package com.example.bondoman.share_preference

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager (var context: Context) {
    val PRIVATE_MODE = 0

    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    var pref:SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor:SharedPreferences.Editor = pref.edit()

    fun setLogin() {
        editor.putBoolean(IS_LOGIN, true)
        editor.commit()
    }

    fun setToken(token: String) {
        editor.putString("token", token)
        editor.commit()
    }

    fun isLogin(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun getToken(): String? {
        return pref.getString("token", "")
    }

    fun removePref() {
        editor.clear()
        editor.commit()
    }

    fun setEmail(email: String?) {
        editor.putString("email", email)
        editor.commit()
    }

    fun getEmail(): String? {
        return pref.getString("email", "")
    }
}