package cn.chitanda.weather.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import cn.chitanda.weather.model.Daily

/**
 *@auther: Chen
 *@createTime: 2020/12/29 20:38
 *@description:
 **/
class DailyTempView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var dailyList = mutableListOf<Daily>()
}