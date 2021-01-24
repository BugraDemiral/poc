package com.monomobile.poc.api

import com.monomobile.poc.model.ArtistItem
import retrofit2.http.GET

interface ApiService {

    @GET("/api/characters")
    suspend fun search() : List<ArtistItem>
}

