package cn.chitanda.weather.model


import com.google.gson.annotations.SerializedName

data class Refer(
    @SerializedName("license")
    val license: List<String>,
    @SerializedName("sources")
    val sources: List<String>
)