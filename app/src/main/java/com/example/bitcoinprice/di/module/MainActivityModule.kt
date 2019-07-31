package com.example.bitcoinprice.di.module

import com.example.bitcoinprice.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): HomeActivity
}