package cn.chitanda.weather.widget.weather.controller

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import cn.chitanda.weather.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:45
 *@description:
 **/
class SunnyController(protected val context: Context) : BaseController() {
    private var rotation = 0f
    private var scale: Float = 1f
    private val minRadius: Float
        get() = width / 7f * scale
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
//                view.postInvalidate()
            }
        }
    }
    private var xAnimator: ValueAnimator? = null
    private var yAnimator: ValueAnimator? = null
    private var scaleAnimator: ValueAnimator? = null
    override fun init(view: View, width: Int, height: Int) {
        super.init(view, width, height)
        resumeAnim()
    }

    override fun setOriginPoint() {
        originPoint.x = width / 2f
        originPoint.y = -width / 4f
    }

    override fun setOrientationAngles(xAngle: Float, yAngle: Float) {
        if (!isInited) return
        if (abs(this.xAngle - xAngle) >= 1) this.xAngle = xAngle
        if (abs(this.yAngle - yAngle) >= 1) this.yAngle = yAngle
        val endX = width / 2 - width * 0.5f * sin(this.xAngle)
        val endY = -width / 4f + abs(width * 0.85f * sin(this.yAngle/2f))

        Log.d(TAG, "setOrientationAngles: x ${this.xAngle} y ${this.yAngle}  scale ${scale}")
        try {
            MainScope().launch {
                withContext(Dispatchers.Main) {
                    yAnimator?.cancel()
                    xAnimator?.cancel()

                    xAnimator = ValueAnimator.ofFloat(originPoint.x, endX).apply {
                        duration = 500
                        interpolator = LinearInterpolator()
                        addUpdateListener { v ->
                            originPoint.x = v.animatedValue as Float
                        }
                        start()
                    }
                    yAnimator = ValueAnimator.ofFloat(originPoint.y, endY).apply {
                        duration = 500
                        interpolator = LinearInterpolator()
                        addUpdateListener { v ->
                            originPoint.y = v.animatedValue as Float
                        }
                        start()
                    }
                    scaleAnimator =
                        ValueAnimator.ofFloat(scale, 1f - 0.4f / 90 * abs(yAngle % 90f)).apply {
                            duration = 500
                            interpolator = LinearInterpolator()
                            addUpdateListener { v ->
                                scale = v.animatedValue as Float
                            }
                            start()
                        }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun draw(canvas: Canvas) {
        drawBackground(canvas, context.getColor(R.color.theme_color))
        for ((i, color) in colors.withIndex().reversed()) {
            polygonPaint.color = color
            drawPolygon(canvas, 10, minRadius * (i + 1), rotation + 10f * i)
        }
    }

    override fun resumeAnim() {
        if (!isInited) return
        if (rotationAnim.isPaused) rotationAnim.resume()
        else rotationAnim.start()
        if (xAnimator?.isPaused == false) xAnimator?.resume()
        if (yAnimator?.isPaused == false) yAnimator?.resume()
        if (scaleAnimator?.isPaused == false) scaleAnimator?.resume()
    }

    override fun stopAnim() {
        rotationAnim.pause()
        if (xAnimator?.isRunning == false) xAnimator?.pause()
        if (yAnimator?.isRunning == false) yAnimator?.pause()
        if (scaleAnimator?.isRunning == false) scaleAnimator?.pause()
    }
}
