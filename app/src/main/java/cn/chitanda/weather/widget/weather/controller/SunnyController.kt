package cn.chitanda.weather.widget.weather.controller

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.animation.LinearInterpolator
import cn.chitanda.weather.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *@auther: Chen
 *@createTime: 2021/1/2 14:45
 *@description:
 **/
class SunnyController(protected val context: Context) : BaseController() {
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
//                view.postInvalidate()
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
        if (!isInited) return
//        if (abs(this.xAngle - xAngle) >= 2)
        this.xAngle = xAngle
//        if (abs(this.yAngle - yAngle) >= 2)
        this.yAngle = yAngle
        val endX = width / 2 - xAngle * (width / 5f * 3) / 90f
        val endY = -minRadius * 2 - yAngle * (width / 5f * 3) / 90f
//        originPoint.x = endX
//        originPoint.y = endY
        try {
            MainScope().launch {
                withContext(Dispatchers.Main) {
                    yAnimator?.cancel()
                    xAnimator?.cancel()

                    xAnimator = ValueAnimator.ofFloat(originPoint.x, endX).apply {
                        duration = 300
                        interpolator = LinearInterpolator()
                        addUpdateListener { v ->
                            originPoint.x = v.animatedValue as Float
//                 view.postInvalidate()
                        }
                        start()
                    }
                    yAnimator = ValueAnimator.ofFloat(originPoint.y, endY).apply {
                        duration = 300
                        interpolator = LinearInterpolator()
                        addUpdateListener { v ->
                            originPoint.y = v.animatedValue as Float
//                 view.postInvalidate()
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
