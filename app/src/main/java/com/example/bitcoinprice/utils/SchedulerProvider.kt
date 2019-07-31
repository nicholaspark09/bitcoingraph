package com.example.bitcoinprice.utils

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider : BaseSchedulerProvider {

    override fun ioScheduler() = Schedulers.io()

    override fun uiScheduler() = AndroidSchedulers.mainThread()
}