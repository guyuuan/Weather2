package cn.chitanda.weather.widget.weather.controller

import android.graphics.Canvas
import cn.chitanda.weather.widget.weather.DynamicWeatherView

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:00
 *@description:
 **/
interface IController {
    fun init(view: DynamicWeatherView, width: Int, height: Int)
    fun setOrientationAngles(xAngle: Float, yAngle: Float)
    fun draw(canvas: Canvas)
    fun resumeAnim()
    fun stopAnim()
}