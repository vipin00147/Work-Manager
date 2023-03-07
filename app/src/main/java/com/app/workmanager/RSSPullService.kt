package com.app.workmanager

import android.R
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log


class RSSPullService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            var count  = 0
            while (true) {
                Log.e("RSSPullService", "Service is running... ${++count}")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()

        val noti = Notification()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            noti.priority = Notification.PRIORITY_MIN
        }
        startForeground(12, noti)
        return START_NOT_STICKY
    }



}