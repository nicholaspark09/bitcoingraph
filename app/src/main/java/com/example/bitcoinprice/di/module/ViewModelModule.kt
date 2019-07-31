package com.example.bitcoinprice.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bitcoinprice.ui.home.HomeViewModel
import com.example.bitcoinprice.viewmodel.BitcoinViewModelFactory
import com.example.bitcoinprice.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: BitcoinViewModelFactory): ViewModelProvider.Factory
}