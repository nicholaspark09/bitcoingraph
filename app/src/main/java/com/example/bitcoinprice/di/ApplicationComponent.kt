package com.example.bitcoinprice.di

import android.app.Application
import com.example.bitcoinnetwork.repo.BitcoinRepositoryContract
import com.example.bitcoinprice.BitcoinPriceApplication
import com.example.bitcoinprice.di.module.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        MainActivityModule::class,
        AndroidInjectionModule::class
    ]
)
interface ApplicationComponent  {

    fun inject(bitcoinPriceApplication: BitcoinPriceApplication)

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun bitcoinRepo(bitcoinRepository: BitcoinRepositoryContract): Builder

        @BindsInstance
        fun application(application: Application): Builder
    }
}