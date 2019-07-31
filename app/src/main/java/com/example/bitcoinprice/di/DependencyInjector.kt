package com.example.bitcoinprice.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.bitcoinnetwork.BitcoinNetwork
import com.example.bitcoinprice.BitcoinPriceApplication
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

object DependencyInjector {

    fun init(application: BitcoinPriceApplication) {
        DaggerApplicationComponent.builder()
            .application(application)
            .bitcoinRepo(BitcoinNetwork.getInstance(application).bitcoinRepository)
            .build()
            .inject(application)
        injectIfLifecycleIsSupported(application)
    }

    fun injectIfLifecycleIsSupported(application: BitcoinPriceApplication) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {}

            override fun onActivityResumed(activity: Activity?) {}

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activity?.let { injectIntoActivity(it) }
            }

        })
    }

    private fun injectIntoActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            AndroidSupportInjection.inject(f)
                        }
                    }, true
                )
        }
    }
}