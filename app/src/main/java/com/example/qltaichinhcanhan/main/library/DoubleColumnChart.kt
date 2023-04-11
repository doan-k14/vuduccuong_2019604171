package com.example.qltaichinhcanhan.main.library

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.qltaichinhcanhan.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class DoubleColumnChart {
    companion object {

        fun createBarChart(
            barChart: BarChart,
            titles: ArrayList<String>,
            data1: ArrayList<Float>,
            data2: ArrayList<Float>,
        ){
            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(true)
            barChart.description.isEnabled = false
            barChart.legend.isEnabled = false

            val groupSpace = 0.33f
            val barSpace = 0.03f
            val barWidth = 0.30f

            val data1Entries = ArrayList<BarEntry>()
            for (i in data1.indices) {
                data1Entries.add(BarEntry(i.toFloat(), data1[i].toFloat()))
            }
            val data2Entries = ArrayList<BarEntry>()
            for (i in data2.indices) {
                data2Entries.add(BarEntry(i.toFloat(), data2[i].toFloat()))
            }

            val barDataSet1 = BarDataSet(data1Entries, "Data 1")
            barDataSet1.color = Color.RED
            barDataSet1.setDrawValues(false)

            val barDataSet2 = BarDataSet(data2Entries, "Data 2")
            barDataSet2.color = Color.GRAY
            barDataSet2.setDrawValues(false)

            val barData = BarData(barDataSet1, barDataSet2)
            barData.barWidth = barWidth

            barChart.data = barData

            barChart.groupBars(0f, groupSpace, barSpace)
            barChart.setFitBars(true)

            val xAxis = barChart.xAxis
            xAxis.axisMinimum = 0f
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.valueFormatter = IndexAxisValueFormatter(titles)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.textColor = Color.BLACK
            xAxis.labelCount = titles.size
            xAxis.setCenterAxisLabels(true)

            val yAxis = barChart.axisLeft
            yAxis.setDrawAxisLine(false)
            yAxis.setDrawLabels(false)
            val yAxisR = barChart.axisRight
            yAxisR.setDrawAxisLine(false)
            yAxisR.setDrawLabels(false)
            yAxis.setDrawZeroLine(true)


            // Khởi tạo danh sách chú thích
            val entries = listOf(
                LegendEntry("Data 1", Legend.LegendForm.CIRCLE, 14f, 8f, null, Color.RED),
                LegendEntry("Data 2", Legend.LegendForm.CIRCLE, 14f, 8f, null, Color.GRAY)
            )

            // Khởi tạo đối tượng Legend
            val legend = barChart.legend
            legend.isEnabled = true
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            barChart.setExtraOffsets(0f, 0f, 0f, 15f) // Cách chú thích với tiêu đề 10px
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.setCustom(entries)

            //
            barChart.setVisibleXRangeMaximum(5f) // Cho phép hiển thị tối đa 6 tháng trên một lần hiển thị
            barChart.moveViewToX(6f) // Hiển thị từ tháng thứ 6


            barChart.setScaleEnabled(false)        // Tắt tính năng zoom
            barChart.setFitBars(true)
            barChart.description.isEnabled = false // Vô hiệu hóa mô tả biểu đồ
            barChart.animateY(1500) // Tạo hiệu ứng hoạt hình cho biểu đồ trong 1,5 giây
            barChart.isDragEnabled = true // Cho phép kéo để xem các tháng khác
            barChart.legend.isEnabled
        }
    }

}