package cn.chitanda.weather.widget.weather.controller

import android.content.Context
import android.graphics.Canvas

/**
 * @Author:       Chen
 * @Date:         2021/1/13 10:24
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class SunnyNightController(context: Context) : BaseController(context) {

    override fun draw(canvas: Canvas) {
        drawBackground(canvas)
    }

    override fun resumeAnim() {
    }

    override fun stopAnim() {
    }
}