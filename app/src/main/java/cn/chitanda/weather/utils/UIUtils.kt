package cn.chitanda.weather.utils

import android.content.res.Resources
import androidx.annotation.ColorInt
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min

/**
 * @Author:       Chen
 * @Date:         2020/12/30 16:50
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */

private val scale: Float get() = Resources.getSystem().displayMetrics.density
val Int.dp: Float
    get() {
        return scale * this
    }
val Float.dp: Float
    get() {
        return scale * this + 0.5f
    }


fun getColorWithAlpha(alpha: Float, @ColorInt baseColor: Int): Int {
    val a = min(255, max(0, (255 * alpha).toInt())).shl(24)
    return a + 0x00ffffff.and(baseColor)
}

fun getRGB(@ColorInt color: Int): IntArray {
    val red = 0xff0000 and color shr 16
    val green = 0xff00 and color shr 8
    val blue = 0xff and color
    return intArrayOf(red, green, blue)
}

fun sin(num: Float) = kotlin.math.sin(num * PI / 180).toFloat()
fun cos(num: Float) = kotlin.math.cos(num * PI / 180).toFloat()
fun tan(num: Float) = kotlin.math.tan(num * PI / 180).toFloat()