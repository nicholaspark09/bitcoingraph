package com.example.bitcoinnetwork

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bitcoinnetwork.repo.BitcoinRepositoryContract
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BitcoinRepositoryAndroidTest {

    lateinit var context: Context
    lateinit var bitcoinRepo: BitcoinRepositoryContract

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context

        bitcoinRepo = BitcoinNetwork.getInstance(context).bitcoinRepository
    }

    @Test
    fun testApi_getData() {
        // Given
        val startDate = "2019-07-28"
        val endDate = "2019-07-29"

        // When
        val data = bitcoinRepo.getBitcoinPrices(startDate, endDate).blockingGet()

        // Then
        assertEquals(2, data.prices.size)
    }
}