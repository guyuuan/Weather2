package cn.chitanda.weather.model


import com.google.gson.annotations.SerializedName

data class Now(
    @SerializedName("cloud")
    val cloud: String,
    @SerializedName("dew")
    val dew: String,
    @SerializedName("feelsLike")
    val feelsLike: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("obsTime")
    val obsTime: String,
    @SerializedName("precip")
    val precip: String,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("temp")
    val temp: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("vis")
    val vis: String,
    @SerializedName("wind360")
    val wind360: String,
    @SerializedName("windDir")
    val windDir: String,
    @SerializedName("windScale")
    val windScale: String,
    @SerializedName("windSpeed")
    val windSpeed: String
)