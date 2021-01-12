package cn.chitanda.weather.model

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
/*
 * @Description 300-400 雨
 *              400-500 雪
 * #linke https://dev.qweather.com/docs/start/icons/
 */
class Type {
    companion object {
        const val Sunny = 100
        const val Cloudy = 101
        const val FewClouds = 102
        const val PartlyCloudy = 103
        const val Overcast = 104
        const val SunnyNight = 150
        const val CloudyNight = 153
        const val OvercastNight = 154
        const val ShowerRain = 300
        const val HeavyShowerRain = 301
        const val Thundershower = 302
        const val HeavyThunderstorm = 303
        const val ThundershowerWithHail = 304
        const val LightRain = 305
        const val ModerateRain = 306
        const val HeavyRain = 307
        const val ExtremeRain = 308
        const val DrizzleRain = 309
        const val Storm = 310
    }
}