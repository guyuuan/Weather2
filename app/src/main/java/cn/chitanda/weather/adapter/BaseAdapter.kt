package cn.chitanda.weather.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author:       Chen
 * @Date:         2020/12/30 11:30
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>() :
    ListAdapter<T, VH>(object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }) {
}