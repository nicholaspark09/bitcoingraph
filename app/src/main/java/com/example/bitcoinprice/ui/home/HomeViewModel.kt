package com.example.bitcoinprice.ui.home

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bitcoinnetwork.data.BitcoinPrice
import com.example.bitcoinnetwork.repo.BitcoinRepositoryContract
import com.example.bitcoinprice.R
import com.example.bitcoinprice.data.AbsentLiveData
import com.example.bitcoinprice.data.NetworkResource
import com.example.bitcoinprice.utils.BaseSchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val INITIAL_BITCOIN_START = "2010-07-18"
const val DATE_FORMAT = "yyyy-MM-dd"

class HomeViewModel @Inject constructor(
    @VisibleForTesting val bitcoinRepository: BitcoinRepositoryContract,
    @VisibleForTesting val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val currentDate = Date()

    var _startDate: String = formatter.format(currentDate.time - TimeUnit.DAYS.toMillis(ChartState.FIVE_DAYS.days))
    var _endDate: String = formatter.format(currentDate)
    val _shouldLookForNewDates = MutableLiveData<Boolean>()

    val prices: LiveData<NetworkResource<List<BitcoinPrice>>> = Transformations.switchMap(_shouldLookForNewDates) { input ->
        if (input == null || _shouldLookForNewDates.value == null) {
            AbsentLiveData.create()
        } else {
            getBitcoinPrices(_startDate, _endDate)
        }
    }

    fun updateWithTime(chartState: ChartState) {
        if (chartState == ChartState.ALL_TIME) {
            _startDate = INITIAL_BITCOIN_START
        } else {
            _startDate = formatter.format(currentDate.time - TimeUnit.DAYS.toMillis(chartState.days))
        }
        _shouldLookForNewDates.postValue(true)
    }

    internal fun getBitcoinPrices(startDate: String, endDate: String): LiveData<NetworkResource<List<BitcoinPrice>>> = MutableLiveData<NetworkResource<List<BitcoinPrice>>>().apply {
        postValue(NetworkResource.Loading())
        bitcoinRepository.getBitcoinPrices(startDate, endDate)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(
                { bitcoinData -> postValue(NetworkResource.Success(bitcoinData.prices))},
                {error -> postValue(NetworkResource.Error(exception = error))}
            )

    }
}

enum class ChartState(val days: Long, @StringRes val displayString: Int)  {
    FIVE_DAYS(5, R.string.five_days),
    THIRTY_DAYS(30, R.string.thirty_days),
    SIXTY_DAYS(60, R.string.sixty_days),
    YEAR(365, R.string.one_year),
    TWO_YEARS(730, R.string.two_years),
    ALL_TIME(-1, R.string.all_time)
}