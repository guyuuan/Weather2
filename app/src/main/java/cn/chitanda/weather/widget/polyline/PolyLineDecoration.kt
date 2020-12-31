package cn.chitanda.weather.widget.polyline

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import cn.chitanda.weather.model.Daily
import cn.chitanda.weather.utils.dp

/**
 * @Author:       Chen
 * @Date:         2020/12/31 15:27
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class PolyLineDecoration : RecyclerView.ItemDecoration() {
    companion object {
        var maxTempDiff = 20
        var midTemp = 0f
    }

    private var maxHeight = 0f
    private val proportion: Float
        get() = maxHeight / maxTempDiff.toFloat()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 3.dp.toFloat()
        color = Color.WHITE
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        maxHeight = parent.height * 0.5f
        Log.d(TAG, "onDraw: proportion = $proportion")
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount) {
            val view = parent[i]
            val daily = view.tag as Daily
            val centerW = (view.left + view.width / 2).toFloat()
            val centerH = (view.top + view.height / 2).toFloat()
            var dy = (midTemp - (daily.tempMax ?: return).toInt()) * proportion
            val currentMaxPoint = PointF(centerW, centerH + dy)
            dy = (midTemp - (daily.tempMin ?: return).toInt()) * proportion
            val currentMinPoint = PointF(centerW, centerH + dy)
            c.drawCircle(currentMaxPoint.x, currentMaxPoint.y, 3.dp.toFloat(), paint)
            c.drawCircle(currentMinPoint.x, currentMinPoint.y, 3.dp.toFloat(), paint)
            Log.d(TAG, "onDraw: $i $currentMaxPoint  $currentMinPoint" )

            if (i + 1 >= parent.childCount) continue
            val view1 = parent[i + 1]
            val daily1 = view.tag as Daily
            val centerW1 = (view1.right -view1.width / 2f)
            val centerH1 = (view1.bottom - view1.height / 2f)
            var dy1 = (midTemp - (daily1.tempMax ?: return).toInt()) * proportion
            val nextMaxPoint = PointF(centerW1, centerH1 + dy1)
            dy1 = (midTemp - (daily1.tempMin ?: return).toInt()) * proportion
            val nextMinPoint = PointF(centerW1, centerH + dy1)
            c.drawLine(currentMaxPoint.x, currentMaxPoint.y, nextMaxPoint.x, nextMaxPoint.y, paint)
            c.drawLine(
                currentMinPoint.x,
                currentMinPoint.y,
                nextMinPoint.x,
                currentMinPoint.y,
                paint
            )
            Log.d(TAG, "onDraw: $i $nextMaxPoint $nextMinPoint")
//            c.drawCircle(nextMaxPoint.x, nextMaxPoint.y, 3.dp.toFloat(), paint)
//            c.drawCircle(nextMinPoint.x, nextMinPoint.y, 3.dp.toFloat(), paint)
        }
    }
}

private const val TAG = "PolyLineDecoration"