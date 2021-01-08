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

class RainOrSnowController(private val context: Context) : BaseController() {
    private val flakeList = mutableListOf<FlakeController>()
    private val paint: Paint
        get() = listOf(Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
        }).random()

    override fun init(view: DynamicWeatherView, width: Int, height: Int) {
        super.init(view, width, height)
        val x = 0..width
        val y = 0..height
        flakeList.clear()
        repeat(100) {
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
        if (abs(this.xAngle - xAngle) > 1) this.xAngle = xAngle
        if (abs(this.yAngle-yAngle) > 1) this.yAngle = yAngle
    }

    override fun draw(canvas: Canvas) {
        drawBackground(canvas, context.getColor(R.color.theme_color))
        flakeList.forEach {
            it.draw(canvas, xAngle, yAngle, paint)
        }
    }

    override fun resumeAnim() {
    }

    override fun stopAnim() {
    }
}