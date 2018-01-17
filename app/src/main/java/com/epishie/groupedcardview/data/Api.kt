package com.epishie.groupedcardview.data

import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("v2/5a275eb23000006e3c0e8a5e")
    fun getItems(): Call<List<Item>>
}