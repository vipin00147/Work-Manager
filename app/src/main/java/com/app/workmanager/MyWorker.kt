package com.app.workmanager

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.R
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.app.myapplication.Pojo.MyPostsPojoResponse
import com.app.myapplication.network.APIServices
import com.app.myapplication.network.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent

import android.app.PendingIntent
import android.os.Bundle







class MyWorker: Worker, Callback<JsonElement> {

    var getPostsCall: Call<JsonElement>? = null
    lateinit var context: Context
    var data : ArrayList<MyPostsPojoResponse.MyPosts>? = null

    constructor(context: Context, workerParams: WorkerParameters) : super(context, workerParams)

    override fun doWork(): Result {
        context = applicationContext
        getApiData()
        return Result.success()
    }

    private fun displayNotification(title: String, task: ArrayList<MyPostsPojoResponse.MyPosts>) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "simplifiedcoding",
                "simplifiedcoding",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("data", "Main Activity is Launched")
        intent.putExtras(bundle)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val activity = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "simplifiedcoding")
                .setAutoCancel(false)
                .setContentTitle("id : "+ data!!.get(0).id.toString())
                .setContentText("Title : "+data!!.get(0).title)
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentIntent(activity)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Body : "+data!!.get(0).body))
        //notificationManager.notify(1, notification.build())
    }

    private fun getApiData(){
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants().BASE_URL)
            .build()

        // Create Service
        val service = retrofit.create(APIServices::class.java)

        GlobalScope.launch {
            getPostsCall = service.getPostData()
            getPostsCall!!.enqueue(this@MyWorker)
        }
    }

    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
        if (response.isSuccessful) {

            val parentJson = JSONObject()
            val json = JSONArray(response.body()!!.toString())
            parentJson.put("data", json)

            val prettyJson = Gson().fromJson<MyPostsPojoResponse>(parentJson.toString(), MyPostsPojoResponse::class.java)

            data = prettyJson.myPosts as ArrayList<MyPostsPojoResponse.MyPosts>

            onSuccess(call, response.code(), response.body()!!.toString())
        }
    }

    private fun onSuccess(call: Call<JsonElement>, code: Int, response: String) {

        if(call == getPostsCall) {
            //Data to be save
            //val parentJson = JSONObject()

            val json = JSONArray(response)
            //displayNotification("My Worker",  json.getJSONObject(0).optString("title"))


            displayNotification("My Worker", data!!)

        }
    }

    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
        Log.d("response", t.message.toString())
    }
}