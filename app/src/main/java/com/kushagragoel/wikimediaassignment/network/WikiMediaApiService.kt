package com.kushagragoel.wikimediaassignment.network

import com.kushagragoel.wikimediaassignment.network.model.WikiMediaOutputBean
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://en.wikipedia.org//w/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface WikiMediaApiService {
    @GET("api.php?action=query&format=json&prop=pageimages%7Cpageterms%7Cinfo&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10&inprop=url")
    fun getSearchList(@Query("gpssearch") searchText: String): Call<WikiMediaOutputBean>?
}

object WikiMediaApi {
    val retrofitService : WikiMediaApiService by lazy {
        retrofit.create(WikiMediaApiService::class.java) }
}