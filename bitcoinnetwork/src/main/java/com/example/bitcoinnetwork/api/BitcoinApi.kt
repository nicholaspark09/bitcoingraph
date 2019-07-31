package com.example.bitcoinnetwork.api

import com.example.bitcoinnetwork.data.BitcoinData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BitcoinApi {

    @GET("bpi/historical/close.json")
    fun getHistoricalPrices(@Query("start") startDate: String, @Query("end") endDate: String): Single<BitcoinData>
}