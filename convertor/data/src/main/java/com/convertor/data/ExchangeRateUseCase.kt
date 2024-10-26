package com.convertor.data

import com.convertor.data.db.ExchangeRateCache
import com.convertor.data.db.ExchangeRateDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ExchangeRateUseCase @Inject constructor (
    val database: ExchangeRateDatabase,
    val exchangeRatesApiService: ExchangeRatesApiService
) {

    companion object {
        private const val TIME_CONSTRAINT= 30 * 60 * 1000
    }

    private val currentTime = System.currentTimeMillis()

    suspend fun getExchangeRates(): Flow<ApiResponse<ExchangeRatesData, String>> {
        val exchangeRateCache =  database.exchangeRateDao()
        return flow {
                emit(ApiResponse.Loading)
                val cachedTimestamp = exchangeRateCache.getTimestamp()
                if (cachedTimestamp != null && (currentTime-cachedTimestamp) < TIME_CONSTRAINT) {
                    emit(ApiResponse.Success(getDataFromDB()))
                }
                else {
                    emit(getExchangeRateDataFromRemote())
                }

        }.flowOn(Dispatchers.IO)
    }

    private suspend fun getExchangeRateDataFromRemote(): ApiResponse<ExchangeRatesData, String> {
        try {
            val apiResponse = exchangeRatesApiService.getExchangeRates()
            when {
                apiResponse.isSuccessful -> {
                    return apiResponse.body()?.let { newRates ->
                        runBlocking {
                            insertExchangeRatesInDatabase(newRates)
                            ApiResponse.Success(newRates)
                        }
                    } ?: apiFallBack()
                }
                else -> {
                    return apiFallBack()
                }

            }
        } catch (e: Exception) {
            return apiFallBack()
        }
    }

    suspend fun apiFallBack(): ApiResponse<ExchangeRatesData,String> {
        val dbData =  getDataFromDB()
        if(dbData != null) return ApiResponse.Success(dbData)
        return ApiResponse.Error("No data available")
    }

    suspend fun getDataFromDB(): ExchangeRatesData {
        return database.exchangeRateDao().getExchangeRate() ?: ExchangeRatesData("", emptyMap())
    }

    suspend fun insertExchangeRatesInDatabase(newExchangeRate: ExchangeRatesData) {
        val dbData = ExchangeRateCache(timestamp = currentTime, exchangeRates = newExchangeRate)
        database.exchangeRateDao().deleteOldExchangeRate()
        database.exchangeRateDao().insertExchangeRate(dbData)
    }

}