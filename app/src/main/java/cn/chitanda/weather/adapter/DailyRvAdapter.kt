package cn.chitanda.weather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.chitanda.weather.databinding.DailyItemBinding
import cn.chitanda.weather.model.Daily
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
//        holder.itemView.tag = getItem(position)
        holder.daily.currentList = currentList
        holder.daily.setPosition(position)
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

}