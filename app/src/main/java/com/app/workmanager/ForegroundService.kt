package com.app.workmanager

import android.os.Build
import android.content.Intent
import android.os.IBinder
import android.R
import android.app.*
import android.content.IntentFilter
import androidx.core.app.NotificationCompat
import androidx.annotation.Nullable

class ForegroundService : Service() {

    val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(serviceChannel)
        }
    }
}