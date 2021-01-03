package cn.chitanda.weather.widget.weather.controller

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import kotlin.math.*

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:04
 *@description:
 **/
abstract class BaseController : IController {
    protected val TAG = "BaseController"
    protected var width = 0
    protected var height = 0
    protected lateinit var view: View
    override fun init(view: View, width: Int, height: Int) {
        this.view = view
        this.width = width
        this.height = height
        calculate()
    }

    abstract fun calculate()
    abstract fun startAnim()
    protected val polygonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /*
     * @Author  chen
     * @Description  绘制多边形
     * @Date 14:51 2021/1/2
     * @Param count 多边形的边数 radius 多边形外接园的半径 rotation 多边形旋转角度
     * @return
     **/
    protected fun drawPolygon(
        canvas: Canvas,
        x: Float,
        y: Float,
        count: Int,
        radius: Float,
        rotation: Float = 0F
    ) {
        if (count < 5) return
        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(rotation)
        val path = Path()
        repeat(count) {
            if (it == 0) {
                //绘制起点
                path.moveTo(radius * cos(360f / count * it), radius * sin(360f / count * it))
            } else {
                path.lineTo(radius * cos(360f / count * it), radius * sin(360f / count * it))
            }
        }
        path.close()
        canvas.drawPath(path, polygonPaint)
        canvas.restore()
    }

    protected fun sin(num: Float) = sin(num * PI / 180).toFloat()
    protected fun cos(num: Float) = cos(num * PI / 180).toFloat()

}