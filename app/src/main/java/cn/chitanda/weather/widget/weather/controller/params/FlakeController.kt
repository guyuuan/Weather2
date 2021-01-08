package cn.chitanda.weather.widget.weather.controller.params

import android.graphics.Canvas
import android.graphics.Paint

/**
 * @Author:       Chen
 * @Date:         2021/1/7 18:02
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
interface FlakeController {
    fun draw(canvas: Canvas, xAngle: Float, yAngle: Float, paint: Paint)
    fun moveTo(xAngle: Float, yAngle: Float)
}