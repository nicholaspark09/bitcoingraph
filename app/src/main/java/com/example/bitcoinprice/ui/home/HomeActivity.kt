package com.example.bitcoinprice.ui.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.bitcoinnetwork.data.BitcoinPrice
import com.example.bitcoinprice.BitcoinPriceApplication
import com.example.bitcoinprice.R
import com.example.bitcoinprice.data.NetworkResourceObserver
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var homeViewModel: HomeViewModel

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        observeViewModel()
        homeViewModel._shouldLookForNewDates.postValue(true)
    }

    private fun observeViewModel() {
        homeViewModel.prices.observe(this, NetworkResourceObserver (
            onSuccess = { prices ->
                Log.d("Prices", prices.toString())
            },
            onFailure = {i,e,t ->

            },
            onLoading = {

            } ))
    }
}
