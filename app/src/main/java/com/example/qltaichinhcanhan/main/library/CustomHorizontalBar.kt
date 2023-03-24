package com.example.qltaichinhcanhan.main.library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.qltaichinhcanhan.main.model.DataChart
import com.example.qltaichinhcanhan.main.model.ItemColor

class CustomHorizontalBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val dataList = mutableListOf<DataChart>()
    private val spacing = 5 // k // khoảng cách giữa các khoảng data (đơn vị pixel)
    fun setData(data: List<DataChart>) {
        dataList.clear()
        dataList.addAll(data)
        invalidate() // gọi hàm invalidate để vẽ lại View
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        if (dataList.isEmpty()) {
            val paint = Paint()
            paint.color = Color.GRAY
            val radius = height / 2f
            canvas.drawCircle(radius, height / 2f, radius, paint)
            canvas.drawRect(radius, 0f, width - radius, height.toFloat(), paint)
            canvas.drawCircle(width - radius, height / 2f, radius, paint)
            return
        }

        val totalValue = dataList.sumOf { it.value.toDouble() }
        val singleValue = (width - (dataList.size - 1) * spacing).toDouble() / totalValue

        var startX = 0f
        val radius = height / 2f
        for ((index, data) in dataList.withIndex()) {
            val paint = Paint()
            paint.color = getColorForValue(data.color, context)
            val width = (data.value * singleValue).toFloat()
            if (index == 0) { // cột data đầu tiên
                canvas.drawCircle(radius, height / 2f, radius, paint)
                canvas.drawRect(radius, 0f, startX + width, height.toFloat(), paint)
            } else if (index == dataList.lastIndex) { // cột data cuối cùng
                canvas.drawRect(startX, 0f, startX + width - radius, height.toFloat(), paint)
                canvas.drawCircle(startX + width - radius, height / 2f, radius, paint)
            } else {
                canvas.drawRect(startX, 0f, startX + width, height.toFloat(), paint)
            }
            startX += width + if (index < dataList.lastIndex) spacing else 0
        }
    }

    private fun getColorForValue(valueIndex: Int, context: Context): Int {
        return ItemColor.getColorForId(context, valueIndex)
    }
}

