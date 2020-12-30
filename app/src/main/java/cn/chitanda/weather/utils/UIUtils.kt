package cn.chitanda.weather.utils

import android.content.res.Resources

/**
 * @Author:       Chen
 * @Date:         2020/12/30 16:50
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */

private val scale: Float get() = Resources.getSystem().displayMetrics.density
val Int.dp: Int
    get() {
        return (scale * this + 0.5f).toInt()
    }
val Float.dp: Int
    get() {
        return (scale * this + 0.5f).toInt()
    }