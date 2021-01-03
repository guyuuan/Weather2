package cn.chitanda.weather.widget.weather.controller

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import kotlin.math.*

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:45
 *@description:
 **/
class SunnyController : BaseController() {
    private var rotation = 0f
    private val minRadius: Float
        get() = width / 8f
    private val colors = listOf<@ColorInt Int>(
        Color.parseColor("#C54B34"),
        Color.parseColor("#C6633C"),
        Color.parseColor("#CB7341"),
        Color.parseColor("#CE8749"),
        Color.parseColor("#CF9C4D"),
        Color.parseColor("#CEB05A"),
        Color.parseColor("#BFC275"),
        Color.parseColor("#A3C9A4")
    )

    override fun calculate() {
//        val start = getRGB(startColor)
//        val end = getRGB(endColor)
//        val c = end - start
//        colors.forEachIndexed { index, _ ->
//            colors[index] =Color.rgb(start[0]+c[0]*index,start[1]+c[1]*index,start[2]+c[2]*index)
//        }
        startAnim()
    }

    override fun draw(canvas: Canvas) {
        polygonPaint.color = colors[7]
        drawPolygon(canvas, width / 2f, 0f, 10, minRadius * 8, rotation+80f)
        polygonPaint.color = colors[6]
        drawPolygon(canvas, width / 2f, 0f, 10, minRadius * 7, rotation+70f)
        polygonPaint.color = colors[5]
        drawPolygon(canvas, width / 2f, 0f, 10, minRadius * 6, rotation+60f)
        polygonPaint.color = colors[4]
        drawPolygon(canvas, width / 2f, 0f, 10, minRadius * 5, rotation+50f)
        polygonPaint.color = colors[3]
        drawPolygon(canvas, width / 2f, 0f, 10, minRadius * 4, rotation+40f)
        polygonPaint.color = colors[2]
        drawPolygon(canvas, width / 2f, 0f, 10, minRadius * 3, rotation+30f)
        polygonPaint.color = colors[1]
        drawPolygon(canvas, width / 2f, 0f, 10, minRadius * 2, rotation+20f)
        polygonPaint.color = colors[0]
        drawPolygon(canvas, width / 2f, 0f, 10, width / 8f,rotation)
    }

    override fun startAnim() {
        ValueAnimator.ofFloat(0f, 360F).apply {
            duration = 1000 * 30
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { value ->
                rotation = value.animatedValue as Float
                view.postInvalidate()
            }
            start()
        }
    }
}
