package cn.chitanda.weather.widget.weather.controller

import android.graphics.Canvas
import android.view.View
import cn.chitanda.weather.model.Weather

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:00
 *@description:
 **/
interface IController {
    fun init(view:View,width:Int,height:Int)
    fun draw(canvas: Canvas)
}