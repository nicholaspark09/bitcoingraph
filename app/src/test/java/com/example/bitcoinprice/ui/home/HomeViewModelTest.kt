package com.example.bitcoinprice.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bitcoinnetwork.data.BitcoinData
import com.example.bitcoinnetwork.data.BitcoinPrice
import com.example.bitcoinnetwork.data.Time
import com.example.bitcoinnetwork.repo.BitcoinRepositoryContract
import com.example.bitcoinprice.utils.ImmediateSchedulerProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class HomeViewModelTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    @MockK lateinit var bitcoinRepo: BitcoinRepositoryContract
    lateinit var homeViewModel: HomeViewModel

    private val schedulerProvider = ImmediateSchedulerProvider()
    private val bitcoinData = BitcoinData(
        bpi = emptyMap(),
        prices = mutableListOf<BitcoinPrice>().apply {
            BitcoinPrice("2019-07-20", 10000.0)
            BitcoinPrice("2019-07-21", 12000.0)
        },
        disclaimer = "",
        time = Time(updated = "", updatedISO = "")
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        homeViewModel = HomeViewModel(bitcoinRepo, schedulerProvider)
    }

    @Test
    fun testNull() {
        assertThat(homeViewModel.bitcoinRepository, notNullValue())
        assertThat(homeViewModel.prices, notNullValue())
    }

    @Test
    fun getBitcoinPrices_checkRepo() {
        val startDate = "2019-07-20"
        val endDate = "2019-07-21"
        givenBitcoinRepoGetsData(startDate, endDate)

        // When
        homeViewModel.getBitcoinPrices(startDate, endDate)

        // Then
        verify { bitcoinRepo.getBitcoinPrices(startDate, endDate) }
    }

    @Test
    fun testUpdateWithTime_checkStartDate() {

        // When
        homeViewModel.updateWithTime(ChartState.FIVE_DAYS)

        homeViewModel._shouldLookForNewDates.observeForever {
            // Then
            assertTrue(it)
        }
    }

    @Test
    fun testUpdateWithTime_setStartDateToInitialBitcoinStart() {
        // When
        homeViewModel.updateWithTime(ChartState.ALL_TIME)

        // Then
        assertEquals(INITIAL_BITCOIN_START, homeViewModel._startDate)
    }

    private fun givenBitcoinRepoGetsData(startDate: String,
                                         endDate: String) {
        every { bitcoinRepo.getBitcoinPrices(startDate, endDate) } returns Single.just(bitcoinData)
    }
}