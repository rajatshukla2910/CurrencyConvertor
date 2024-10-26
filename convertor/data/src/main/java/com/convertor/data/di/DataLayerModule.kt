package com.convertor.data.di

import android.content.Context
import androidx.room.Room
import com.convertor.data.ExchangeRatesApiService
import com.convertor.data.ExchangeRateUseCase
import com.convertor.data.db.ExchangeRateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent:: class)
@Module
object DataLayerModule {

    @Provides
    fun provideExchangeRateApiService(): ExchangeRatesApiService {
        return Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ExchangeRatesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideExchangeRateDatabase(@ApplicationContext context: Context): ExchangeRateDatabase {
        return Room.databaseBuilder(
            context,
            ExchangeRateDatabase::class.java, "exchange-rates-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExchangeRateUseCase(database: ExchangeRateDatabase, exchangeRatesApiService: ExchangeRatesApiService): ExchangeRateUseCase {
        return ExchangeRateUseCase(database, exchangeRatesApiService)
    }
}