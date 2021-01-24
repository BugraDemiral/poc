package com.monomobile.poc

import com.monomobile.poc.api.*
import com.monomobile.poc.model.ArtistItem
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import retrofit2.HttpException
import org.junit.Assert.*

@RunWith(JUnit4::class)
class ApiRepositoryTest {
    private val responseHandler = ResponseHandler()
    private lateinit var apiService: ApiService
    private lateinit var repository: ApiRepository
    private val artistItemList = listOf(ArtistItem(
        appearance = listOf("1"),
        name = "Adam Pinkman"
    ))
    private val apiResponse = Resource.success(artistItemList)
    private val errorResponse = Resource.error("Unauthorised", null)

    @Before
    fun setUp() {
        apiService = mock()

        repository = ApiRepository(
            apiService,
            responseHandler
        )
    }

    @Test
    fun `test search for valid artistItem is returned`() {
        runBlocking {
            `when`(apiService.search()).thenReturn(artistItemList)
            assertEquals(apiResponse, repository.getSearchResults())
        }
    }

    @Test
    fun `test search for error is returned on http HttpException`() {
        runBlocking {
            val mockException: HttpException = mock()
            `when`(mockException.code()).thenReturn(401)
            `when`(apiService.search()).thenThrow(mockException)

            assertEquals(errorResponse, repository.getSearchResults())
        }
    }

}