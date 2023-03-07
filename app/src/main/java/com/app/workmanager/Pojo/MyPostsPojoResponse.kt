package com.app.myapplication.Pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyPostsPojoResponse {
    @SerializedName("data")
    @Expose
    var myPosts: List<MyPosts>? = null

    class MyPosts {
        @SerializedName("userId")
        @Expose
        var userId: Int? = null

        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("body")
        @Expose
        var body: String? = null
    }
}