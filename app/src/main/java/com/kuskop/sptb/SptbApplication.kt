package com.kuskop.sptb

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.kuskop.sptb.core.database.SptbApplicationHolder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SptbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SptbApplicationHolder.context = this
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_PERMOHONAN,
                    "Permohonan Baru",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Notifikasi permohonan baru dan kemaskini status" }
            )
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_KEPUTUSAN,
                    "Keputusan",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Notifikasi keputusan kelulusan" }
            )
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_PERINGATAN,
                    "Peringatan",
                    NotificationManager.IMPORTANCE_LOW
                ).apply { description = "Peringatan SLA dan jadual" }
            )
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_GENERAL,
                    "SPTB Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Notifikasi umum sistem" }
            )
        }
    }

    companion object {
        const val CHANNEL_PERMOHONAN = "sptb_permohonan"
        const val CHANNEL_KEPUTUSAN = "sptb_keputusan"
        const val CHANNEL_PERINGATAN = "sptb_peringatan"
        const val CHANNEL_GENERAL = "sptb_notifications"
    }
}
