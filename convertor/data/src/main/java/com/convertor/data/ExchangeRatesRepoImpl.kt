//package com.convertor.data
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.flowOn
//import java.io.IOException
//import java.net.SocketTimeoutException
//import javax.inject.Inject
//
//class ExchangeRatesRepoImpl @Inject constructor(val service: ExchangeRatesApiService): ExchangeRatesRepo {
//
//    override suspend fun getExchangeRates(): Flow<ApiResponse<ExchangeRatesData, String>> {
//
//        return flow {
//            emit(ApiResponse.Loading)
//            try {
//                val response = service.getExchangeRates()
//                when {
//                    response.isSuccessful -> {
//                        val body = response.body()
//                        if (body == null) emit(ApiResponse.Error("No Data available"))
//                        else {
//                            emit(ApiResponse.Success(body))
//                        }
//                    }
//
//                    else ->
//                        emit(ApiResponse.Error("Server Error" + response.code()))
//                }
//            } catch (e: SocketTimeoutException) {
//                emit(ApiResponse.Error("Network timeout. Please try again later."))
//            } catch (e: IOException) {
//                emit(ApiResponse.Error("Network error. Please check your internet connection."))
//            }
//        }.flowOn(Dispatchers.IO)
//
//    }
//}