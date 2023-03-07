package com.app.workmanager

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        processIntent(intent)

        buttonStartService.setOnClickListener{
            startService(it)
        }
        buttonStopService.setOnClickListener {
            stopService(it)
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(Intent(applicationContext, RSSPullService::class.java))
        } else {
            applicationContext.startService(Intent(applicationContext, RSSPullService::class.java))
        }

    }

    //Start Running Service
    fun startService(view : View) {

        if(isLocationServiceRunning()) {
            Snackbar.make(view,"Service is Already Running", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.RED)
                .setActionTextColor(Color.WHITE)
                .setAction("Dismiss",View.OnClickListener {})
                .show()
        }
        else {
            val serviceIntent = Intent(this, ForegroundService::class.java)
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android")
            ContextCompat.startForegroundService(this, serviceIntent)

            Snackbar.make(view,"Service Started", Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.green))
                .setActionTextColor(Color.WHITE)
                .setAction("Dismiss",View.OnClickListener {})
                .show()
        }
    }

    //Stop Running Service
    fun stopService(view : View) {

        val serviceIntent = Intent(this, ForegroundService::class.java)

        if(isLocationServiceRunning()) {
            stopService(serviceIntent)

            Snackbar.make(view,"Service Stopped", Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.green))
                .setActionTextColor(Color.WHITE)
                .setAction("Dismiss",View.OnClickListener {})
                .show()
        }
        else{
            Snackbar.make(view,"Service is Already Stopped", Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.RED)
                .setActionTextColor(Color.WHITE)
                .setAction("Dismiss",View.OnClickListener {})
                .show()
        }
    }

    //Check if Service is Running or Not
    private fun isLocationServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (ForegroundService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    private fun processIntent(intent: Intent) {
        //get your extras
    }
}