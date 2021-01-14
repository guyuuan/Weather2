package cn.chitanda.weather.widget.weather.controller.params

import android.content.res.Resources
import android.graphics.*
import cn.chitanda.weather.utils.dp
import cn.chitanda.weather.utils.sin

/**
 * @Author:       Chen
 * @Date:         2021/1/7 17:24
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class SnowFlake private constructor(
    private var x: Float,
    private var y: Float,
    private val radius: Float,

    ) : FlakeController {
    companion object {
        private var refreshRate: Long = 1000L / 60

        //        private val widths = listOf(1.5f.dp, 2f.dp, 2.5f.dp)
//        private val heights = listOf(40.dp, 55.dp, 70.dp)
        private val speeds: Float
            get() = screenHeight / listOf(2000, 2500, 1500).random().dp * refreshRate
        private val radius: Float
            get() = listOf(13.dp, 17.dp, 20.dp).random()

        private val screenWidth: Int
            get() = Resources.getSystem().displayMetrics.widthPixels
        private val screenHeight: Int
            get() = Resources.getSystem().displayMetrics.heightPixels
        private val alphas: Int
            get() = (listOf(0.3f, 0.5f, 0.8f).random() * 255).toInt()
//        private val colors: Int
//            get() = (listOf(
//                Color.WHITE,
//                Color.parseColor("#CCB527"),
//                Color.WHITE,
//                Color.parseColor("#B6BF67")
//            )).random()

        fun create(x: Float, y: Float, refreshRate: Long): SnowFlake {
            this.refreshRate = refreshRate
            return SnowFlake(x, y, radius)
        }
    }

    private val speed = speeds
    private val alpha = alphas
    private val color = Color.WHITE
    override fun draw(canvas: Canvas, xAngle: Float, yAngle: Float, paint: Paint) {
        paint.apply {
//            strokeWidth = w
            color = this@SnowFlake.color
            shader = RadialGradient(
                x,
                y,
                radius,
                Color.parseColor("#A0FFFFFF"),
                Color.parseColor("#10FFFFFF"),
                Shader.TileMode.CLAMP
            )
//            alpha = this@SnowFlake.alpha
        }
        canvas.drawCircle(x, y, radius, paint)
        moveTo(xAngle, yAngle)
    }

    override fun moveTo(xAngle: Float, yAngle: Float) {
        y += speed * sin(-yAngle)
        x += speed * sin(xAngle)
        if (y.toInt() !in -screenHeight / 10..screenHeight / 10 * 11 || x.toInt() !in -screenWidth / 3..screenWidth / 3 * 4) {
            y = (-screenHeight / 10..0).random().toFloat()
            x = (-screenWidth / 3..screenWidth / 3 * 4).random().toFloat()
        }
    }
}