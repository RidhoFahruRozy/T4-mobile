package com.example.studentdirectoryapp.utils

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DARK_MODE    = "dark_mode"
        private const val KEY_NOTIFICATION = "notification_enabled"
    }

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var isNotificationEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATION, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATION, value).apply()
}
