package com.example.bitcoinprice.di.module

import com.example.bitcoinprice.utils.BaseSchedulerProvider
import com.example.bitcoinprice.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [MainActivityModule::class])
class AppModule {

    @Provides
    @Singleton
    fun baseSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider()
}