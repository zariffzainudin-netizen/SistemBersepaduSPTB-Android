package com.kuskop.sptb.core.network

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kuskop.sptb.MainActivity
import com.kuskop.sptb.R
import com.kuskop.sptb.SptbApplication

class SptbFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to backend for push targeting
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
            ?: message.data["title"]
            ?: "Sistem Bersepadu SPTB"
        val body = message.notification?.body
            ?: message.data["body"]
            ?: ""
        val type = message.data["type"] ?: "general"
        val deepLink = message.data["deepLink"] ?: ""

        val channelId = when (type) {
            "permohonan" -> SptbApplication.CHANNEL_PERMOHONAN
            "keputusan" -> SptbApplication.CHANNEL_KEPUTUSAN
            "peringatan" -> SptbApplication.CHANNEL_PERINGATAN
            else -> SptbApplication.CHANNEL_GENERAL
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (deepLink.isNotEmpty()) {
                data = android.net.Uri.parse(deepLink)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(this).notify(
                System.currentTimeMillis().toInt(),
                notification,
            )
        } catch (e: SecurityException) {
            // Notification permission not granted
        }
    }
}
