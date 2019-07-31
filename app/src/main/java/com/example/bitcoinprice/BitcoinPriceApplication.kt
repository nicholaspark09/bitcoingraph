package com.example.bitcoinprice

import android.app.Activity
import android.app.Application
import com.example.bitcoinnetwork.repo.BitcoinRepository
import com.example.bitcoinprice.di.DependencyInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BitcoinPriceApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        DependencyInjector.init(this)
    }
}