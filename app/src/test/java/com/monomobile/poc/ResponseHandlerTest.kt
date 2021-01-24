package com.monomobile.poc

import com.monomobile.poc.api.ResponseHandler
import com.monomobile.poc.model.ArtistItem
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

@RunWith(JUnit4::class)
class ResponseHandlerTest {
    lateinit var responseHandler: ResponseHandler

    @Before
    fun setUp() {
        responseHandler = ResponseHandler()
    }

    @Test
    fun `when exception code is 401 then return unauthorised`() {
        val httpException = HttpException(Response.error<ArtistItem>(401, mock(ResponseBody::class.java)))
        val result = responseHandler.handleException<ArtistItem>(httpException)
        assertEquals("Unauthorised", result.message)
    }

    @Test
    fun `when timeout then return timeout error`() {
        val socketTimeoutException = SocketTimeoutException()
        val result = responseHandler.handleException<ArtistItem>(socketTimeoutException)
        assertEquals("Timeout", result.message)
    }
}