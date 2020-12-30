package cn.chitanda.weather.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * @Author:       Chen
 * @Date:         2020/12/30 17:42
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class WeatherPolyLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawPoly(it)
        }
    }

    private fun drawPoly(canvas: Canvas) {}
}