package com.example.qltaichinhcanhan.main.retrofit

import com.example.qltaichinhcanhan.main.model.CountryResponse
import retrofit2.http.GET

interface CountryService {
    @GET("v2/all")
    suspend fun getAllCountries(): List<CountryResponse>
}
