package cn.chitanda.weather.widget.weather.controller.params

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import cn.chitanda.weather.utils.dp
import cn.chitanda.weather.utils.sin

/**
 * @Author:       Chen
 * @Date:         2021/1/7 17:24
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class RainFlake private constructor(
    private var x: Float,
    private var y: Float,
    private val w: Float,
    private val h: Float,

    ) : FlakeController {
    companion object {
        private var refreshRate: Long = 1000L / 60
        private val widths = listOf(1.dp, 1.5f.dp, 2.dp)
        private val heights = listOf(35.dp, 50.dp, 65.dp)
        private val speeds: Float
            get() = screenHeight / listOf(200, 300, 400).random().dp * refreshRate
        private val lineWidth: Float
            get() = widths.random()
        private val lineHeight: Float
            get() = heights.random().toFloat()
        private val screenWidth: Int
            get() = Resources.getSystem().displayMetrics.widthPixels
        private val screenHeight: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
        private val alphas: Int
            get() = (listOf(0.3f, 0.5f, 0.8f).random() * 255).toInt()

        fun create(x: Float, y: Float, refreshRate: Long): RainFlake {
            this.refreshRate = refreshRate
            return RainFlake(x, y, lineWidth, lineHeight)
        }
    }

    private fun init() {

    }


    private val speed = speeds
    private val alpha = alphas

    override fun draw(canvas: Canvas, xAngle: Float, yAngle: Float, paint: Paint) {
        paint.apply {
            strokeWidth = w
            alpha = this@RainFlake.alpha
        }
        val dx = x + sin(-(xAngle + 10f) % 90) * h
        val dy = y + sin((yAngle + 10f) % 90) * h
        canvas.drawLine(x, y, dx, dy, paint)
        moveTo((xAngle + 10f) % 90, (yAngle+10f)%90)
    }

    override fun moveTo(xAngle: Float, yAngle: Float) {
        y += speed * sin(-yAngle)
        x += speed * sin(xAngle)
        if (y.toInt() !in 0..screenHeight || x.toInt() !in 0..screenWidth) {
            y = (0..screenHeight).random().toFloat()
            x = (0..screenWidth).random().toFloat()
        }
    }
}