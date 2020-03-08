package com.example.currencyapp.data.api

import com.example.currencyapp.data.model.CurrencyRatesResponse
import io.reactivex.Observable
import retrofit2.http.*

interface ApiClient {

    @GET("/api/android/latest")
    fun getCurrencyRates(
        @Query("base") base: String
    ): Observable<CurrencyRatesResponse>
}