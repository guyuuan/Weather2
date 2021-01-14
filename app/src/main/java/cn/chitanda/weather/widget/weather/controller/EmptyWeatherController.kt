package cn.chitanda.weather.widget.weather.controller

import android.content.Context
import android.graphics.Canvas
import cn.chitanda.weather.R

/**
 * @Author:       Chen
 * @Date:         2021/1/13 10:33
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class EmptyWeatherController(context: Context):BaseController(context) {
    override fun draw(canvas: Canvas) {
        drawBackground(canvas,context.getColor(R.color.theme_color))
    }

    override fun resumeAnim() {
    }

    override fun stopAnim() {
    }
}