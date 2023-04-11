package com.example.qltaichinhcanhan.main.library

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.qltaichinhcanhan.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class ColumnChartUtils {
    companion object {
        private val defaultColors = listOf(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6,
            R.color.color7,
            R.color.bg_color
        )

        fun createBarChart(
            barChart: BarChart,
            colorId: ArrayList<Int>,
            titles: ArrayList<String>,
            data: ArrayList<Float>,
        ) {

            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(true)
            barChart.description.isEnabled = false
            barChart.legend.isEnabled = false

            val dataEntries = ArrayList<BarEntry>()
            for (i in data.indices) {
                dataEntries.add(BarEntry(i.toFloat(), data[i].toFloat()))
            }
            val colors = ArrayList<Int>()
            for (i in colorId.indices) {
                val colorIndex = colorId[i].toInt() - 1
                colors.add(ContextCompat.getColor(barChart.context, defaultColors[colorIndex]))
            }

            val barDataSet = BarDataSet(dataEntries, "Data")
            barDataSet.colors = colors
            barDataSet.setDrawValues(true)
            barDataSet.valueTextColor = Color.BLACK
            barDataSet.valueTextSize = 10f

            val barData = BarData(barDataSet)
            val barWidth = 0.45f
            barData.barWidth = barWidth
            barChart.data = barData

            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = IndexAxisValueFormatter(titles)
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.labelCount = titles.size

            val yAxis = barChart.axisLeft
            yAxis.setDrawAxisLine(false)
            yAxis.setDrawLabels(false)
            val yAxisR = barChart.axisRight
            yAxisR.setDrawAxisLine(false)
            yAxisR.setDrawLabels(false)
            yAxis.setDrawZeroLine(true)

            barChart.setVisibleXRangeMaximum(4f)
            barChart.moveViewToX(1f)

            barChart.setScaleEnabled(false)
            barChart.setFitBars(true)
            barChart.description.isEnabled = false
            barChart.animateY(1000)
            barChart.isDragEnabled = true
        }
    }
}
