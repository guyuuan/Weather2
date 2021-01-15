package cn.chitanda.weather.model

import cn.chitanda.weather.R

/**
 * @Author:       Chen
 * @Date:         2020/12/29 17:58
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
data class Weather(
    val updateTime: Long,
    val location: Location,
    val now: Now?,
    val daily: List<Daily>?,
    val hourly: List<Hourly>?
)

fun weatherIconSelector(icon: Int) = when (icon) {
    100 -> R.drawable.ic_sunny
    101, 102, 103 -> R.drawable.ic_cloudy
    104 -> R.drawable.ic_overcast
    150 -> R.drawable.ic_sunny_night
    300, 301 -> R.drawable.ic_shower
    302, 303 -> R.drawable.ic_thundershower
    304 -> R.drawable.ic_hail
    305, 306, 314, 350, 351, 399 -> R.drawable.ic_rain
    307, 308, 310, 311, 312, 315, 316, 317, 318 -> R.drawable.ic_downpour
    309 -> R.drawable.ic_drizzle
    400, 401, 407, 457, 499 -> R.drawable.ic_snow
    402, 408, 409 -> R.drawable.ic_flurry
    403, 410 -> R.drawable.ic_snowstorm
    404, 405, 406, 456 -> R.drawable.ic_sleet
    500, 501,509,510,514,515 -> R.drawable.ic_fog
    502,511,512,513->R.drawable.ic_haze
    503,504,507,508->R.drawable.ic_sandstorm
    900->R.drawable.ic_hot
    else -> R.drawable.ic_empty
}