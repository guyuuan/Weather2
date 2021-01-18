package cn.chitanda.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.chitanda.weather.R
import cn.chitanda.weather.databinding.DailyItemBinding
import cn.chitanda.weather.model.Daily
import cn.chitanda.weather.model.weatherIconSelector
import cn.chitanda.weather.widget.polyline.WeatherPolyLineView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * @Author:       Chen
 * @Date:         2020/12/31 14:38
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
private const val TAG = "DailyRvAdapter"

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DailyRvAdapter : BaseAdapter<Daily, DailyItemViewHolder>() {
    private val originTimeFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyItemViewHolder {
        if (!this::weekDays.isInitialized) weekDays =
            parent.context.resources.getStringArray(R.array.week_days)
        return DailyItemViewHolder(
            DailyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: DailyItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.daily.setPosition(position)
        }
    }

    override fun onBindViewHolder(holder: DailyItemViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            daily.apply {
                currentList = this@DailyRvAdapter.currentList
                setPosition(position)
            }
            date.text = if (position == 0) {
                item.fxDate?.substring(5)
            } else {
                item.fxDate?.let { getWeekDay(it) }
            }
            textDay.text = item.textDay
            textNight.text = item.textNight
            iconDay.setImageResource(weatherIconSelector(item.iconDay?.toInt() ?: -1))
            iconNight.setImageResource(weatherIconSelector(item.iconNight?.toInt() ?: -1))
        }

    }

    private lateinit var weekDays: Array<String>
    private fun getWeekDay(time: String): String {
        val date = Date(originTimeFormat.parse(time).time)
        val w = Calendar.getInstance().apply {
            setTime(date)
        }.get(Calendar.DAY_OF_WEEK) - 1
        if (w < 0) return weekDays[0]
        return weekDays[w]
    }

    override fun submitList(list: MutableList<Daily>?) {
        var max = Int.MIN_VALUE
        var min = Int.MAX_VALUE
        list?.forEach {
            max = max(max, (it.tempMax ?: Int.MAX_VALUE.toString()).toInt())
            min = min(min, (it.tempMin ?: Int.MIN_VALUE.toString()).toInt())
        }
        WeatherPolyLineView.maxTempDiff = max - min
        WeatherPolyLineView.midTemp = (max + min) / 2f
        super.submitList(list)
    }
}

class DailyItemViewHolder(binding: DailyItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val daily = binding.daily
    val date = binding.date
    val textDay = binding.textDay
    val textNight = binding.textNight
    val iconDay = binding.iconDay
    val iconNight = binding.iconNight
}