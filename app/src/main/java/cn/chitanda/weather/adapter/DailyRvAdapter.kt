package cn.chitanda.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.chitanda.weather.databinding.DailyItemBinding
import cn.chitanda.weather.model.Daily
import cn.chitanda.weather.model.weatherIconSelector
import cn.chitanda.weather.widget.polyline.WeatherPolyLineView
import kotlin.math.max
import kotlin.math.min

/**
 * @Author:       Chen
 * @Date:         2020/12/31 14:38
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
private const val TAG = "DailyRvAdapter"

class DailyRvAdapter : BaseAdapter<Daily, DailyItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyItemViewHolder {
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
            date.text = item.fxDate
            textDay.text = item.textDay
            textNight.text = item.textNight
            iconDay.setImageResource(weatherIconSelector(item.iconDay?.toInt() ?: -1))
            iconNight.setImageResource(weatherIconSelector(item.iconNight?.toInt() ?: -1))
        }

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