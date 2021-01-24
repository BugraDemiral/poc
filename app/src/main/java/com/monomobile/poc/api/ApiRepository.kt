package com.monomobile.poc.api

import com.monomobile.poc.model.ArtistItem
import io.reactivex.Observable

class ApiRepository(private val apiService: ApiService,
                    private val responseHandler: ResponseHandler) {

    suspend fun getSearchResults(): Resource<List<ArtistItem>> {
        return try {
            val response = apiService.search()
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}