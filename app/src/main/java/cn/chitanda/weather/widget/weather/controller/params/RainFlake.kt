package cn.chitanda.weather.widget.weather.controller.params

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
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
        private val widths = listOf(1.5f.dp, 2f.dp, 2.5f.dp)
        private val heights = listOf(40.dp, 55.dp, 70.dp)
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
        private val colors: Int
            get() = (listOf(
                Color.WHITE,
                Color.parseColor("#CCB527"),
                Color.WHITE,
                Color.parseColor("#B6BF67")
            )).random()

        fun create(x: Float, y: Float, refreshRate: Long): RainFlake {
            this.refreshRate = refreshRate
            return RainFlake(x, y, lineWidth, lineHeight)
        }
    }

    private val speed = speeds
    private val alpha = alphas
    private val color = colors
    override fun draw(canvas: Canvas, xAngle: Float, yAngle: Float, paint: Paint) {
        paint.apply {
            strokeWidth = w
            color = this@RainFlake.color
            alpha = this@RainFlake.alpha
        }
        val dx = x + sin(-xAngle) * h
        val dy = y + sin(yAngle) * h
        canvas.drawLine(x, y, dx, dy, paint)
        moveTo(xAngle, yAngle)
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