package com.convertor.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.convertor.data.ExchangeRatesData


@Entity(tableName = "exchange_rates")
data class ExchangeRateCache(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exchangeRates: ExchangeRatesData,
    val timestamp: Long
)
