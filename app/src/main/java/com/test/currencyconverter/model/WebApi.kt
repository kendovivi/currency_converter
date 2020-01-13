package com.test.currencyconverter.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://apilayer.net/api/"
const val ACCESS_KEY = "802f94c303291b392a67abde35e1c639"

fun getCurrencyLive(): Single<CurrencyLive> {
    return getRetrofit().create(CurrencyApi::class.java).getCurrencyLive(ACCESS_KEY)
}

private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}


