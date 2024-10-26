package com.convertor.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ExchangeRateCache::class], version = 1)
@TypeConverters(Converters::class)
abstract class ExchangeRateDatabase: RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}