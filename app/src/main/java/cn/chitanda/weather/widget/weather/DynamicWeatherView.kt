package cn.chitanda.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import cn.chitanda.weather.widget.weather.controller.IController

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
    private var isMeasured = false
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidth = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val viewHeight = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)
        weatherController?.init(this, viewWidth, viewHeight)
        isMeasured = true
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        canvas?.let {
            weatherController?.draw(it)
        }
    }

    override fun postInvalidate() {
        if (!isMeasured) return
        super.postInvalidate()
    }

    enum class Type {
        Sunny, Cloudy, Rain, Snow,
    }
}