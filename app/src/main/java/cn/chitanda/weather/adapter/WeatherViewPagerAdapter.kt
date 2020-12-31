package cn.chitanda.weather.adapter

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.chitanda.weather.databinding.WeatherItemBinding
import cn.chitanda.weather.model.Daily
import cn.chitanda.weather.model.Weather
import cn.chitanda.weather.widget.polyline.PolyLineDecoration

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
        holder.swipe.setOnRefreshListener {
            holder.swipe.isRefreshing = false
        }
        holder.header.apply {
            holder.group.requestLayout()
            layoutParams = layoutParams.apply {
                height = holder.paddingTop
                Log.d("adapter", "onBindViewHolder: h ${holder.paddingTop}")
            }
        }
        holder.dailyTemp.apply {
            adapter = DailyRvAdapter().apply {
                submitList(item.daily as MutableList<Daily>?)
            }
            layoutManager =
                LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }
//            addItemDecoration(PolyLineDecoration())
        }
    }

}

class WeatherItemViewHolder(binding: WeatherItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val paddingTop: Int
        get() = Resources.getSystem().displayMetrics.heightPixels - nowText.layoutParams.height - nowTemp.layoutParams.height - dailyTemp.layoutParams.height
    val swipe = binding.swipeRefresh
    val group = binding.group
    val header = binding.emptyHeader
    val nowTemp = binding.nowTemp
    val nowText = binding.nowText
    val todayTemp = binding.todayTemp
    val dailyTemp = binding.dailyTempView
    val animWeather = binding.dynamicWeatherView
}