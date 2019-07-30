package com.example.bitcoinnetwork.data

data class BitcoinData(
    val bpi: Map<String, Double>,
    val prices: List<BitcoinPrice> = emptyList(),
    val disclaimer: String,
    val time: Time
)

data class Time(
    val updated: String,
    val updatedISO: String
)