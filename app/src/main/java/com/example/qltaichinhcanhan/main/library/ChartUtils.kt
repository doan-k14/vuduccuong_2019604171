package com.example.qltaichinhcanhan.main.library


import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.model.m_convert.FilterTransactions
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.DecimalFormat

object ChartUtils {
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

    fun pieChart(
        context: Context,
        data: List<FilterTransactions>,
        pieChart: PieChart,
        type: String,
    ) {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        val colorArray = defaultColors

        var sumAmount = 0F
        if (data.isNotEmpty()) {
            for (i in data) {
                var totalMoney = 0F
                if (i.listTransaction.isNotEmpty()) {
                    for (j in i.listTransaction) {
                        totalMoney += j.transactionWithDetails?.transaction?.transactionAmount!! / j.moneyAccountWithDetails?.country?.exchangeRate!!
                    }
                }
                entries.add(PieEntry(totalMoney))
                colors.add(ContextCompat.getColor(context,
                    colorArray[i.transaction!!.transactionWithDetails?.category!!.color!! - 1]))
                sumAmount += totalMoney
            }
        } else {
            colors.add(ContextCompat.getColor(context, colorArray[7]))
            colors.add(colorArray[7])
            entries.add(PieEntry(100F))
        }

        val dataSet = PieDataSet(entries, "Data")
        dataSet.colors = colors
        dataSet.sliceSpace = 1.5f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 0f
        val pieData = PieData(dataSet)
        pieChart.invalidate()
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setUsePercentValues(false)
        pieChart.setTouchEnabled(false)
        pieChart.holeRadius = 70f
        if (sumAmount != 0F) {
            val formatter = DecimalFormat("#,###")
            pieChart.centerText = "${formatter.format(sumAmount)} ${type}"
            pieChart.animateY(1500, Easing.EaseInOutQuad)
        } else {
            pieChart.centerText = context.resources.getString(R.string.you_don_t_have_data_yet)
            pieChart.animateY(1500, Easing.EaseInOutQuad)
        }
        pieChart.setCenterTextSize(14f)
        pieChart.setCenterTextColor(Color.BLACK)
    }
}
