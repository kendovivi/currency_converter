package com.test.currencyconverter.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

public interface CurrencyApi {
    @GET("live")
    fun getCurrencyLive(@Query("access_key") accessKey: String): Single<CurrencyLive>

    @GET("list")
    fun getAvailableCurrencies(@Query("access_key") accessKey: String): Single<CurrencyLive>
}