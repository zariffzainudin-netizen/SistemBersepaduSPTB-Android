package com.pkk.sistembersepadusptbpkkhq.core.datastore

import android.content.SharedPreferences
import com.pkk.sistembersepadusptbpkkhq.core.di.RegularPrefs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsDataStore @Inject constructor(
    @RegularPrefs private val prefs: SharedPreferences
) {
    var darkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var dynamicColor: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_COLOR, true)
        set(value) = prefs.edit().putBoolean(KEY_DYNAMIC_COLOR, value).apply()

    var sfxVolume: Float
        get() = prefs.getFloat(KEY_SFX_VOLUME, 0.7f)
        set(value) = prefs.edit().putFloat(KEY_SFX_VOLUME, value).apply()

    var lastSeenVersion: String?
        get() = prefs.getString(KEY_LAST_SEEN_VERSION, null)
        set(value) = prefs.edit().putString(KEY_LAST_SEEN_VERSION, value).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    fun clearCache() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_DARK_MODE = "dark_mode"
        const val KEY_DYNAMIC_COLOR = "dynamic_color"
        const val KEY_SFX_VOLUME = "sfx_volume"
        const val KEY_LAST_SEEN_VERSION = "last_seen_version"
        const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
    }
}
