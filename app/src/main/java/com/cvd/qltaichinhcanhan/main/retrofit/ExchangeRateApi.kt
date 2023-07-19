package com.cvd.qltaichinhcanhan.main.retrofit

import com.cvd.qltaichinhcanhan.main.model.query_model.ExchangeRate
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("latest/{base}")
    suspend fun getExchangeRate(@Path("base") baseCurrency: String): ExchangeRate
}
