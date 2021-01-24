package com.monomobile.poc.api

import com.monomobile.poc.model.ArtistItem
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface ApiService {

    @GET("/api/characters")
    suspend fun search() : List<ArtistItem>
}

