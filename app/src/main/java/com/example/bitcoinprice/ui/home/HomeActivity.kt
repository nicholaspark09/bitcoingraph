package com.example.bitcoinprice.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.bitcoinnetwork.data.BitcoinPrice
import com.example.bitcoinprice.BitcoinPriceApplication
import com.example.bitcoinprice.R
import com.example.bitcoinprice.data.NetworkResourceObserver
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
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

        setupViewModel()
        homeViewModel._shouldLookForNewDates.postValue(true)
        setSpinnerAdapter()
        setChartSettings()

        updateChartButton.setOnClickListener {
            val selectedState = ChartState.values()[homeSpinner.selectedItemPosition]
            homeViewModel.updateWithTime(selectedState)
        }
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        homeViewModel.prices.observe(this, NetworkResourceObserver (
            onSuccess = { prices ->
                homeProgressBar.visibility = View.GONE
                if (prices != null) {
                    setData(prices)
                }
            },
            onFailure = {exception, errorCode, message ->
                homeProgressBar.visibility = View.GONE
                if (exception != null) {
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            },
            onLoading = {
                homeProgressBar.visibility = View.VISIBLE
            } ))
    }

    private fun setSpinnerAdapter() {
        val timeValues = mutableListOf<String>()
        ChartState.values().forEach {
            timeValues.add(getString(it.displayString))
        }
        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, timeValues)
        homeSpinner.adapter = adapter
    }

    private fun setChartSettings() {
        homeLineChart.apply {
            setBackgroundColor(Color.WHITE)
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(false)
            isDragEnabled = true
            setPinchZoom(true)
        }
    }

    private fun setData(prices: List<BitcoinPrice>) {
        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()
        prices.forEachIndexed { index, price ->
            val entry = Entry(index.toFloat(), price.price.toFloat())
            if (prices.size <= 60) {
                labels.add(price.date.takeLast(5))
            } else {
                labels.add(price.date.substring(0, 7))
            }
            entries.add(entry)
        }

        val lineDataSet = LineDataSet(entries, getString(R.string.bitcoin))
        lineDataSet.lineWidth = 3f

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(lineDataSet)

        setXAxisLabels(labels)

        homeLineChart.data = LineData(dataSets)
        homeLineChart.invalidate()
    }

    private fun setXAxisLabels(labels: List<String>) {
        val xAxis = homeLineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setLabelCount(5, false)
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
    }
}
