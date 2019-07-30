package com.example.bitcoinnetwork.repo

import com.example.bitcoinnetwork.data.BitcoinData
import com.example.bitcoinnetwork.data.BitcoinPrice
import io.reactivex.Single
import java.util.*

interface BitcoinRepositoryContract {

    fun getBitcoinPrices(startDate: String, endDate: String): Single<BitcoinData>
}