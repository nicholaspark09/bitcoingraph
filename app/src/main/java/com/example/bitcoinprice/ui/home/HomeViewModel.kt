package com.example.bitcoinprice.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bitcoinnetwork.data.BitcoinPrice
import com.example.bitcoinnetwork.repo.BitcoinRepositoryContract
import com.example.bitcoinprice.data.AbsentLiveData
import com.example.bitcoinprice.data.NetworkResource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val INITIAL_START = 5L

class HomeViewModel @Inject constructor(
    @VisibleForTesting val bitcoinRepository: BitcoinRepositoryContract
) : ViewModel() {

    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = Date()

    var _startDate: String = formatter.format(currentDate.time - TimeUnit.DAYS.toMillis(INITIAL_START))
    var _endDate: String = formatter.format(currentDate)
    val _shouldLookForNewDates = MutableLiveData<Boolean>()

    val prices: LiveData<NetworkResource<List<BitcoinPrice>>> = Transformations.switchMap(_shouldLookForNewDates) { input ->
        if (input == null || _shouldLookForNewDates.value == null) {
            AbsentLiveData.create()
        } else {
            getBitcoinPrices(_startDate, _endDate)
        }
    }

    private fun getBitcoinPrices(startDate: String, endDate: String): LiveData<NetworkResource<List<BitcoinPrice>>> = MutableLiveData<NetworkResource<List<BitcoinPrice>>>().apply {
        postValue(NetworkResource.Loading())
        bitcoinRepository.getBitcoinPrices(startDate, endDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { bitcoinData -> postValue(NetworkResource.Success(bitcoinData.prices))},
                {error -> postValue(NetworkResource.Error(exception = error))}
            )

    }
}