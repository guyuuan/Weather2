package cn.chitanda.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import cn.chitanda.weather.widget.weather.controller.IController
import kotlin.math.PI

/**
 * @Author:       Chen
 * @Date:         2020/12/30 10:07
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
private const val TAG = "DynamicWeatherView"

class DynamicWeatherView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var weatherController: IController? = null
//        set(value) {
//            field = value
//            field?.init(width, height)
//            postInvalidate()
//        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidth = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val viewHeight = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)
        weatherController?.init(this,viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        canvas?.let {
            weatherController?.draw(it)
        }
    }

    enum class Type {
        Sunny, Cloudy, Rain, Snow,
    }
}