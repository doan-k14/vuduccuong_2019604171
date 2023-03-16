package com.example.qltaichinhcanhan.main.ui.accounts

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.qltaichinhcanhan.databinding.FragmentAccountsBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.github.aachartmodel.aainfographics.aachartcreator.*

class AccountsFragment : BaseFragment() {

    lateinit var binding: FragmentAccountsBinding
    lateinit var aaChartModel: AAChartModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNavigation.setOnClickListener {
            myCallback?.onCallback()
        }

//        // data theo 12 tháng
        val monthTitles1 = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")

        val data12: Array<Any> =
            arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)
        val data22: Array<Any> =
            arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)

        setDataChart(monthTitles1, data12, data22)
        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)

    }

    fun setDataChart(monthTitles: Array<String>, data1: Array<Any>, data2: Array<Any>) {
        aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .backgroundColor("#FFFFFF")
            .tooltipEnabled(false)  // hiện thị thống kê
            .categories(monthTitles) // hiện thị tháng
            .yAxisLabelsEnabled(false) // giá trị của trục oy
            .yAxisGridLineWidth(0f)
            .yAxisTitle("")

            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(data1),
                AASeriesElement()
                    .name("NewYork")
                    .data(data2),
            ))
    }

}

