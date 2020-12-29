package cn.chitanda.weather.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("adm1")
    val adm1: String,
    @SerializedName("adm2")
    val adm2: String,
    @SerializedName("cnty")
    val cnty: String,
    @SerializedName("fxLink")
    val fxLink: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isDst")
    val isDst: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("rank")
    val rank: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("tz")
    val tz: String,
    @SerializedName("utcOffset")
    val utcOffset: String
)