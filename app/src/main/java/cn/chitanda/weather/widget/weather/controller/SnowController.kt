package cn.chitanda.weather.widget.weather.controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import cn.chitanda.weather.R
import cn.chitanda.weather.widget.weather.DynamicWeatherView
import cn.chitanda.weather.widget.weather.controller.params.FlakeController
import cn.chitanda.weather.widget.weather.controller.params.SnowFlake

/**
 * @Author:       Chen
 * @Date:         2021/1/13 11:00
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class SnowController(context: Context, private val snowType: Int = 400) : BaseController(context) {
    private val count: Int
        get() = when (snowType) {
            400, 408, 499 -> 40
            401, 407, 409 -> 65
            else -> 80
        }
    private val snowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val snowList = mutableListOf<FlakeController>()
    override fun init(view: DynamicWeatherView, width: Int, height: Int) {
        super.init(view, width, height)
        snowList.clear()
        val x = -width / 3..width / 3 * 4
        val y = 0..height
        repeat(count) {
            snowList.add(SnowFlake.create(x.random().toFloat(), y.random().toFloat(), view.refreshRate))
        }
    }

    override fun draw(canvas: Canvas) {
        drawBackground(canvas, context.getColor(R.color.theme_color))
        snowList.forEach {
            it.draw(canvas, xAngle, yAngle, snowPaint)
        }
    }

    override fun resumeAnim() {
    }

    override fun stopAnim() {
    }
}