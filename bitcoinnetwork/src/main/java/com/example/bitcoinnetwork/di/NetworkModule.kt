package com.example.bitcoinnetwork.di

import com.example.bitcoinnetwork.BuildConfig
import com.example.bitcoinnetwork.api.BitcoinApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalStateException

class NetworkModule private constructor(
    val url: String
){

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().also {
            it.addNetworkInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                )
            )
        }.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val bitcoinApi: BitcoinApi = retrofit.create(BitcoinApi::class.java)

    companion object {

        @Volatile
        private var INSTANCE: NetworkModule ?= null

        @JvmStatic
        fun intialize(url: String) {
            if (INSTANCE == null) {
                synchronized(NetworkModule::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = NetworkModule(url)
                    }
                }
            }
        }

        @JvmStatic
        fun getInstance(): NetworkModule {
            return INSTANCE ?:
            throw IllegalStateException("You must call initialize with the url to use this network")
        }

    }
}