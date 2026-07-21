package com.pkk.sistembersepadusptbpkkhq.core.datastore

import android.content.SharedPreferences
import com.pkk.sistembersepadusptbpkkhq.core.di.EncryptedPrefs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    @EncryptedPrefs private val encryptedPrefs: SharedPreferences
) {
    var authToken: String?
        get() = encryptedPrefs.getString(KEY_AUTH_TOKEN, null)
        set(value) = encryptedPrefs.edit().putString(KEY_AUTH_TOKEN, value).apply()

    var userEmail: String?
        get() = encryptedPrefs.getString(KEY_USER_EMAIL, null)
        set(value) = encryptedPrefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var userName: String?
        get() = encryptedPrefs.getString(KEY_USER_NAME, null)
        set(value) = encryptedPrefs.edit().putString(KEY_USER_NAME, value).apply()

    var userRole: String?
        get() = encryptedPrefs.getString(KEY_USER_ROLE, null)
        set(value) = encryptedPrefs.edit().putString(KEY_USER_ROLE, value).apply()

    var biometricEnabled: Boolean
        get() = encryptedPrefs.getBoolean(KEY_BIOMETRIC_ENABLED, true)
        set(value) = encryptedPrefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, value).apply()

    var biometricFailCount: Int
        get() = encryptedPrefs.getInt(KEY_BIOMETRIC_FAIL_COUNT, 0)
        set(value) = encryptedPrefs.edit().putInt(KEY_BIOMETRIC_FAIL_COUNT, value).apply()

    val isLoggedIn: Boolean get() = authToken != null

    fun clear() {
        encryptedPrefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_AUTH_TOKEN = "auth_token"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_ROLE = "user_role"
        const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        const val KEY_BIOMETRIC_FAIL_COUNT = "biometric_fail_count"
    }
}
