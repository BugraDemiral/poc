package com.monomobile.poc

import com.monomobile.poc.api.ApiRepository
import com.monomobile.poc.api.ApiService
import com.monomobile.poc.api.ResponseHandler
import com.monomobile.poc.ui.main.MainViewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val API_URL = "https://breakingbadapi.com"

val networkModule = module {
    single { provideRetrofit() }
    factory { ResponseHandler() }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(API_URL)
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

val mainViewModelModule = module {
    factory { provideApiService(get()) }
    factory { ApiRepository(get(), get()) }
    factory { MainViewModel(get()) }
}