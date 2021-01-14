package cn.chitanda.weather.widget.weather.controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import cn.chitanda.weather.R
import cn.chitanda.weather.widget.weather.DynamicWeatherView
import cn.chitanda.weather.widget.weather.controller.params.FlakeController
import cn.chitanda.weather.widget.weather.controller.params.RainFlake
import kotlin.math.abs

/**
 * @Author:       Chen
 * @Date:         2021/1/7 15:34
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */

class RainController(context: Context, private val rainType: Int = 305) :
    BaseController(context) {
    private val flakeList = mutableListOf<FlakeController>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }
    private val isThunderShower
        get() = when (rainType) {
            302, 303, 304 -> true
            else -> false
        }
    private val count: Int
        get() = when (rainType) {
            300, 305, 309, 314, 350, 399 -> 40
            302, 303, 304, 306, 315 -> 70
            else -> 100
        }

    override fun init(view: DynamicWeatherView, width: Int, height: Int) {
        super.init(view, width, height)
        val x = 0..width
        val y = 0..height
        flakeList.clear()
        repeat(count) {
            flakeList.add(
                RainFlake.create(
                    x.random().toFloat(),
                    y.random().toFloat(),
                    view.refreshRate
                )
            )
        }
    }

    override fun setOrientationAngles(xAngle: Float, yAngle: Float) {
        if (!isInited) return
        if (abs(this.xAngle - xAngle) > 2) this.xAngle = xAngle
        if (abs(this.yAngle - yAngle) > 2) this.yAngle = yAngle
    }

    override fun draw(canvas: Canvas) {
        drawBackground(canvas, context.getColor(R.color.rain_background))
        flakeList.forEach {
            it.draw(canvas, xAngle, yAngle, paint)
        }
    }

    override fun resumeAnim() {
    }

    override fun stopAnim() {
    }
}