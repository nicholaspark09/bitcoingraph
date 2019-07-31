package com.example.bitcoinprice.utils

import io.reactivex.Scheduler

interface BaseSchedulerProvider {
    fun ioScheduler(): Scheduler
    fun uiScheduler(): Scheduler
}