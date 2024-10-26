package com.convertor.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.convertor.data.ExchangeRatesData

@Dao
interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRate(exchangeRateCache: ExchangeRateCache)

    @Query("DELETE FROM exchange_rates")
    suspend fun deleteOldExchangeRate()

    @Query("SELECT exchangeRates FROM exchange_rates")
    suspend fun getExchangeRate(): ExchangeRatesData?


    @Query("SELECT timestamp FROM exchange_rates")
    suspend fun getTimestamp(): Long?
}