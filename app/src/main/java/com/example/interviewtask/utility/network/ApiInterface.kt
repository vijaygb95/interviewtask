package com.kotlintest.app.network

import com.example.interviewtask.model.StoreDetailsModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("item/{ITEM_ID}.json")
    suspend fun getItem(@Path("ID")  ITEM_ID:String): StoreDetailsModel

      @GET("newstories.json")
    suspend fun getNewstories(): List<String>

    @GET("topstories.json")
    suspend fun getTopstories(): List<String>

    @GET("beststories.json")
    suspend fun getBeststories(): List<String>

    @GET("askstories.json")
    suspend fun getAskstories(): List<String>

    @GET("showstories.json")
    suspend fun getShowstories(): List<String>

    @GET("jobstories.json")
    suspend fun getJobstories(): List<String>
}