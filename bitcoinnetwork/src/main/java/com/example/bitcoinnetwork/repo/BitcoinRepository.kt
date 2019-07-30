package com.example.bitcoinnetwork.repo

import com.example.bitcoinnetwork.api.BitcoinApi
import com.example.bitcoinnetwork.data.BitcoinData
import com.example.bitcoinnetwork.data.BitcoinPrice
import io.reactivex.Single

class BitcoinRepository(
    private val bitcoinApi: BitcoinApi
) : BitcoinRepositoryContract {

    override fun getBitcoinPrices(startDate: String, endDate: String): Single<BitcoinData> {
        return fetchBitcoinData(startDate, endDate)
            .flatMap { bitcoinData ->
                val prices = mutableListOf<BitcoinPrice>()
                bitcoinData.bpi.forEach { map ->
                    prices.add(BitcoinPrice(map.key, map.value))
                }
                Single.just(bitcoinData.copy(prices = prices))
            }
    }

    internal fun fetchBitcoinData(startDate: String, endDate: String): Single<BitcoinData> {
        return bitcoinApi.getHistoricalPrices(startDate, endDate)
    }

}