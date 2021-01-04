package cn.chitanda.weather.widget.weather.controller

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:45
 *@description:
 **/
class SunnyController : BaseController() {
    private var rotation = 0f
    private val minRadius: Float
        get() = width / 8f
    private val colors = listOf(
        Color.parseColor("#C54B34"),
        Color.parseColor("#C6633C"),
        Color.parseColor("#CB7341"),
        Color.parseColor("#CE8749"),
        Color.parseColor("#CF9C4D"),
        Color.parseColor("#CEB05A"),
        Color.parseColor("#BFC275"),
        Color.parseColor("#A3C9A4")
    )
    private val rotationAnim: ValueAnimator by lazy {
        ValueAnimator.ofFloat(0f, 360F).apply {
            duration = 1000 * 30
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { value ->
                rotation = value.animatedValue as Float
                view.postInvalidate()
            }
        }
    }
    private var xAnimator: ValueAnimator? = null
    private var yAnimator: ValueAnimator? = null
    override fun init(view: View, width: Int, height: Int) {
        super.init(view, width, height)
        resumeAnim()
    }

    override fun setOriginPoint() {
        originPoint.x = width / 2f
        originPoint.y = -minRadius * 2
    }

    override fun setOrientationAngles(xAngle: Float, yAngle: Float) {
        super.setOrientationAngles(xAngle, yAngle)
        val endX = width / 2 - xAngle * (width / 2f) / 90f
        val endY = -minRadius * 2 - yAngle * (width / 2f) / 90f
        xAnimator?.cancel()
        yAnimator?.cancel()
        xAnimator = ValueAnimator.ofFloat(originPoint.x, endX).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { v ->
                originPoint.x = v.animatedValue as Float
                view.postInvalidate()
            }
            start()
        }
        yAnimator = ValueAnimator.ofFloat(originPoint.y, endY).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { v ->
                originPoint.y = v.animatedValue as Float
                view.postInvalidate()
            }
            start()
        }
    }

    override fun draw(canvas: Canvas) {
//        polygonPaint.color = colors[7]
//        drawPolygon(canvas, 10, minRadius * 8, rotation + 80f)
//        polygonPaint.color = colors[6]
//        drawPolygon(canvas, 10, minRadius * 7, rotation + 70f)
//        polygonPaint.color = colors[5]
//        drawPolygon(canvas, 10, minRadius * 6, rotation + 60f)
//        polygonPaint.color = colors[4]
//        drawPolygon(canvas, 10, minRadius * 5, rotation + 50f)
//        polygonPaint.color = colors[3]
//        drawPolygon(canvas, 10, minRadius * 4, rotation + 40f)
//        polygonPaint.color = colors[2]
//        drawPolygon(canvas, 10, minRadius * 3, rotation + 30f)
//        polygonPaint.color = colors[1]
//        drawPolygon(canvas, 10, minRadius * 2, rotation + 20f)
//        polygonPaint.color = colors[0]
//        drawPolygon(canvas, 10, width / 8f, rotation)
        for ((i, color) in colors.withIndex().reversed()) {
            polygonPaint.color = color
            drawPolygon(canvas, 10, minRadius * (i + 1), rotation + 10f * i)
        }
    }

    override fun resumeAnim() {
        if (!isInited) return
        rotationAnim.start()
        if (xAnimator?.isPaused == false) xAnimator?.resume()
        if (yAnimator?.isPaused == false) yAnimator?.resume()
    }

    override fun stopAnim() {
        rotationAnim.pause()
        if (xAnimator?.isRunning == false) xAnimator?.pause()
        if (yAnimator?.isRunning == false) yAnimator?.pause()
    }
}
