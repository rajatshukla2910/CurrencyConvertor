package com.convertor.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.convertor.data.ExchangeRatesData

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromExchangeRatesData(data: ExchangeRatesData): String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun toExchangeRatesData(data: String): ExchangeRatesData {
        return gson.fromJson(data, ExchangeRatesData::class.java)
    }

    @TypeConverter
    fun fromRatesMap(rates: Map<String, Double>): String {
        return gson.toJson(rates)
    }

    @TypeConverter
    fun toRatesMap(data: String): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(data, type)
    }
}
