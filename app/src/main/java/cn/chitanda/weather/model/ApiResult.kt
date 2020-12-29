package cn.chitanda.weather.model

import com.google.gson.annotations.SerializedName

/**
 * @Author:       Chen
 * @Date:         2020/12/29 11:53
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
data class ApiResult(
    @SerializedName("code")
    val code: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("now")
    val now: Now?,

    @SerializedName("daily")
    val daily: List<Daily>?,

    @SerializedName("hourly")
    val hourly: List<Hourly>?,

    @SerializedName("updateTime")
    val updateTime: String?,

    @SerializedName("location")
    val locations: List<Location>?,

    @SerializedName("topCityList")
    val topCityList: List<Location>?,

    @SerializedName("fxLink")
    val fxLink: String?,

    @SerializedName("refer")
    val refer: Refer?,
)
