package cn.chitanda.weather.model

/**
 * @Author:       Chen
 * @Date:         2020/12/29 17:58
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
data class Weather(
    val updateTime: String,
    val location: Location,
    val now: Now,
    val daily: List<Daily>,
    val hourly: List<Hourly>
)