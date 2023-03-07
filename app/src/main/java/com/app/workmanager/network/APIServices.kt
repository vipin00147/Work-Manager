package com.app.myapplication.network


import com.google.gson.JsonElement
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface APIServices {
    @GET("posts")
    fun getPostData(): Call<JsonElement>
}