package com.example.currency

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface currencyAPI {
    @GET("latest")
    suspend fun getRates(
        @Query("access_key") apiKey: String,
        @Query("base") baseCurrency: String
    ): Response<currencyResponse>
}