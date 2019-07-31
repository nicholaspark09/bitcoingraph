package com.example.bitcoinprice.utils

import io.reactivex.schedulers.Schedulers

/**
 *  This scheduler provider is for tests only
 *  Trampoline executes all tasks in FIFO order
 */
class ImmediateSchedulerProvider : BaseSchedulerProvider {

    override fun ioScheduler() = Schedulers.trampoline()

    override fun uiScheduler() = Schedulers.trampoline()
}