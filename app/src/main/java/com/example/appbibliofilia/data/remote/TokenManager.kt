package com.example.appbibliofilia.data.remote

import android.content.Context

class TokenManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "prefs_auth"
        private const val KEY_TOKEN = "key_token"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}

