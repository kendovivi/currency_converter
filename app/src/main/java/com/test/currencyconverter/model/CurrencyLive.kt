package com.test.currencyconverter.model

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonObject

class CurrencyLive {
    @SerializedName("success")
    val success: Boolean? = null
    @SerializedName("term")
    val term: String? = null
    @SerializedName("privacy")
    val privacy: String? = null
    @SerializedName("timestamp")
    val timestamp: Double? = null
    @SerializedName("source")
    val source: String? = null
    @SerializedName("quotes")
    private var quotes: JsonObject? = null

    fun getRateMap(): Map<String, Float> {
        val type = object : TypeToken<Map<String, Float>>() {}.type
        val tempMap: Map<String, Float> = Gson().fromJson(quotes, type)

        val resultMap = mutableMapOf<String, Float>()
        tempMap.forEach {
            val key = it.key.substring(3)
            resultMap[key] = it.value
        }
        return resultMap
    }

}