package cn.chitanda.weather.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.chitanda.weather.databinding.WeatherItemBinding
import cn.chitanda.weather.model.Daily
import cn.chitanda.weather.model.Weather

/**
 * @Author:       Chen
 * @Date:         2020/12/30 11:43
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
class WeatherViewPagerAdapter : BaseAdapter<Weather, WeatherItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.nowTemp.text = (item.now?.temp ?: "-") + " ℃"
        holder.nowText.text = item.now?.text ?: "-"
        holder.todayTemp.text =
            "${item.daily?.first()?.tempMin ?: "-"} / ${item.daily?.first()?.tempMax ?: "-"} ℃"

        holder.dailyTemp.apply {
            adapter = DailyRvAdapter().apply {
                submitList(item.daily as MutableList<Daily>?)
            }
            layoutManager =
                LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }
//            addItemDecoration(PolyLineDecoration())
        }
        holder.header.apply {
            layoutParams = layoutParams.apply {
//                postDelayed({ height = holder.paddingTop }, 200)
                height = holder.paddingTop
            }
        }
    }

}

class WeatherItemViewHolder(binding: WeatherItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val paddingTop: Int
        get() = Resources.getSystem().displayMetrics.heightPixels - nowText.layoutParams.height - nowTemp.layoutParams.height - dailyTemp.layoutParams.height/2
    val group = binding.group
    val header = binding.emptyHeader
    val nowTemp = binding.nowTemp
    val nowText = binding.nowText
    val todayTemp = binding.todayTemp
    val dailyTemp = binding.dailyTempView

}