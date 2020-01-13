package com.test.currencyconverter.common

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.test.currencyconverter.model.CurrencyLive

private const val LOGTAG = "SharedPrefsUtil"

fun saveCurrencyRate(context: Context, currencyLive: CurrencyLive) {
    val editor = PreferenceManager.getDefaultSharedPreferences(context.applicationContext).edit()
    val rateString: String = Gson().toJson(currencyLive, CurrencyLive::class.java)
    editor.putString("rate", rateString)
    editor.putLong("last_request_time", System.currentTimeMillis())
    editor.commit()
}

fun getCurrencyRate(context: Context): CurrencyLive? {
    val rateString = PreferenceManager.getDefaultSharedPreferences(context.applicationContext).getString("rate", null)
    return Gson().fromJson(rateString, CurrencyLive::class.java)
}

//fun saveFetchTimeMills(context: Context) {
//    val editor = PreferenceManager.getDefaultSharedPreferences(context.applicationContext).edit()
//
//    editor.commit()
//}

fun isValidRequestTime(context: Context, interval: Int): Boolean {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    return if (!prefs.contains("last_request_time")) {
        Log.d(LOGTAG, "need to request rate!")
        true
    }
    else {
        val lastRequestTimeMills = prefs.getLong("last_request_time", 0L)

        val nowTimeMills = System.currentTimeMillis()
        val diffMills = nowTimeMills - lastRequestTimeMills
        val intervalMills = 1000 * 60 * interval
        if (diffMills > intervalMills) {
            Log.d(LOGTAG, "over request interval, will request rate!")
            true
        } else {
            Log.d(LOGTAG, "will request rate again after ${(intervalMills - diffMills) / 1000} secs")
            false
        }

    }
}