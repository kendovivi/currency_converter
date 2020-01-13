package com.test.currencyconverter.model

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson

class CurrencyList {
    @SerializedName("success")
    val success: Boolean? = null
    @SerializedName("term")
    val term: String? = null
    @SerializedName("privacy")
    val privacy: String? = null
    @SerializedName("timestamp")
    val timestamp: Double? = null
    @SerializedName("currencies")
    private val currencies: String? = null

    fun getCurrencyMap(): Map<String, String> {
        val gson = Gson()
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(currencies, type)
    }

}