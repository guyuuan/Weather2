package cn.chitanda.weather.model


import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("fxDate")
    val fxDate: String?,
    @SerializedName("humidity")
    val humidity: String?,
    @SerializedName("iconDay")
    val iconDay: String?,
    @SerializedName("iconNight")
    val iconNight: String?,
    @SerializedName("moonrise")
    val moonrise: String?,
    @SerializedName("moonset")
    val moonset: String?,
    @SerializedName("precip")
    val precip: String?,
    @SerializedName("pressure")
    val pressure: String?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("tempMax")
    val tempMax: String?,
    @SerializedName("tempMin")
    val tempMin: String?,
    @SerializedName("textDay")
    val textDay: String?,
    @SerializedName("textNight")
    val textNight: String?,
    @SerializedName("uvIndex")
    val uvIndex: String?,
    @SerializedName("vis")
    val vis: String?,
    @SerializedName("wind360Day")
    val wind360Day: String?,
    @SerializedName("wind360Night")
    val wind360Night: String?,
    @SerializedName("windDirDay")
    val windDirDay: String?,
    @SerializedName("windDirNight")
    val windDirNight: String?,
    @SerializedName("windScaleDay")
    val windScaleDay: String?,
    @SerializedName("windScaleNight")
    val windScaleNight: String?,
    @SerializedName("windSpeedDay")
    val windSpeedDay: String?,
    @SerializedName("windSpeedNight")
    val windSpeedNight: String?
)