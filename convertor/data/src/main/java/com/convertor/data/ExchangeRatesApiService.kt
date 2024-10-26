package com.convertor.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesApiService {
    @GET("latest.json")
    suspend fun getExchangeRates(@Query("app_id") id: String = "0c685214e9964c13a0164f412a1da21a"): Response<ExchangeRatesData>
}