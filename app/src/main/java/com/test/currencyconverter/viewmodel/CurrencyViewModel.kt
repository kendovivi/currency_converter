package com.test.currencyconverter.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.currencyconverter.model.CurrencyLive
import com.test.currencyconverter.model.getCurrencyLive
import com.test.currencyconverter.common.getCurrencyRate
import com.test.currencyconverter.common.isValidRequestTime
import com.test.currencyconverter.common.saveCurrencyRate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CurrencyViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        /* API読み込みのmin間隔 単位:min */
        private const val RATE_REQUEST_INTERVAL = 30

        private const val LOGTAG = "CurrencyViewModel"
    }

    private var currencyLive: MutableLiveData<CurrencyLive> = MutableLiveData()
    private var rateMapData: MutableLiveData<Map<String, Float>> = MutableLiveData()
    private var loadingStatus: MutableLiveData<LoadingStatus> = MutableLiveData()
    private var currentSelectedCurrency: String = "USD"

    fun loadCurrency(): LiveData<CurrencyLive> {
        loadingStatus.postValue(LoadingStatus.Loading)

        if (needFetchData()) {
            requestCurrency()
        } else {
            getCurrencyRate(getApplication())?.let {
                loadingStatus.postValue(LoadingStatus.Success)
                currencyLive.postValue(it)
            } ?: run {
                requestCurrency()
            }
        }
        return currencyLive
    }

    fun getRateMapData(): LiveData<Map<String, Float>> {
        return rateMapData
    }

    fun getLoadingStatus(): LiveData<LoadingStatus> {
        return loadingStatus
    }

    private fun requestCurrency() {
        Log.d(LOGTAG, "requestCurrency")
        getCurrencyLive()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<CurrencyLive>() {
                override fun onSuccess(value: CurrencyLive?) {
                    value?.let {
                        loadingStatus.postValue(LoadingStatus.Success)
                        saveCurrencyRate(getApplication(), it)
                        currencyLive.postValue(value)
                    } ?: run {
                        loadingStatus.postValue(LoadingStatus.Error)
                    }
                }

                override fun onError(e: Throwable?) {
                    Log.d(LOGTAG, "on Error $e")
                    loadingStatus.postValue(LoadingStatus.Error)
                }
            })
    }

    private fun needFetchData(): Boolean {
        if (isValidRequestTime(getApplication(), RATE_REQUEST_INTERVAL)) {
            return true
        }
        return false
    }

    fun updateSelectedCurrency(selectedStr: String) {
        val rateMap = currencyLive.value?.getRateMap()
        val selectedCurrencyRateMap = mutableMapOf<String, Float>()
        if (rateMap?.contains(selectedStr) == true) {
            currentSelectedCurrency = selectedStr
            rateMap.forEach {
                selectedCurrencyRateMap[it.key] = (rateMap[it.key])!!  / (rateMap[currentSelectedCurrency])!!
            }
            rateMapData.postValue(selectedCurrencyRateMap)
        }
    }

    enum class LoadingStatus {
        Loading,
        Success,
        Error
    }
}