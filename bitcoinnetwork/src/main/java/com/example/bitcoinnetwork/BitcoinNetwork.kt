package com.example.bitcoinnetwork

import android.content.Context
import com.example.bitcoinnetwork.di.NetworkModule
import com.example.bitcoinnetwork.repo.BitcoinRepository
import com.example.bitcoinnetwork.repo.BitcoinRepositoryContract

class BitcoinNetwork {

    val bitcoinRepository: BitcoinRepositoryContract by lazy {
        BitcoinRepository(NetworkModule.getInstance().bitcoinApi)
    }

    companion object {

        @Volatile
        private var INSTANCE: BitcoinNetwork?= null

        @JvmStatic
        fun getInstance(context: Context): BitcoinNetwork {
            if (INSTANCE == null) {
                synchronized(BitcoinNetwork::class.java) {
                    if (INSTANCE == null) {
                        initializeDependencies(context)
                        INSTANCE = BitcoinNetwork()
                    }
                }
            }
            return INSTANCE!!
        }

        @JvmStatic
        private fun initializeDependencies(context: Context) {
            NetworkModule.intialize(context.getString(R.string.endpoint))
        }
    }
}