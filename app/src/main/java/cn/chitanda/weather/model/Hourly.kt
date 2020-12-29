package cn.chitanda.weather.model


import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("cloud")
    val cloud: String?,
    @SerializedName("dew")
    val dew: String?,
    @SerializedName("fxDate")
    val fxDate: String?,
    @SerializedName("humidity")
    val humidity: String?,
    @SerializedName("ice")
    val ice: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("pop")
    val pop: String?,
    @SerializedName("precip")
    val precip: String?,
    @SerializedName("pressure")
    val pressure: String?,
    @SerializedName("snow")
    val snow: String?,
    @SerializedName("temp")
    val temp: String?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("wind360")
    val wind360: String?,
    @SerializedName("windDir")
    val windDir: String?,
    @SerializedName("windScale")
    val windScale: String?,
    @SerializedName("windSpeed")
    val windSpeed: String?
)