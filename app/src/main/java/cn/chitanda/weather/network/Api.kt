package cn.chitanda.weather.network

import cn.chitanda.weather.model.ApiResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author:       Chen
 * @Date:         2020/12/29 13:07
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
interface Api {
    @GET("/v7/weather/now")
    fun getNowWeather(
        @Query("location") location: String,
        @Query("key") key: String = "2133f20e86f744acbf8d016fe0b2c8a4"
    ): Call<ApiResult>

    @GET("https://geoapi.qweather.com/v2/city/lookup")
    fun getCity(
        @Query("location") location: String,
        @Query("key") key: String = "2133f20e86f744acbf8d016fe0b2c8a4"
    ): Call<ApiResult>

    @GET("/v7/weather/7d")
    fun getDaily(
        @Query("location") location: String,
        @Query("key") key: String = "2133f20e86f744acbf8d016fe0b2c8a4"
    ): Call<ApiResult>

    @GET("/v7/weather/24h")
    fun getHourly(
        @Query("location") location: String,
        @Query("key") key: String = "2133f20e86f744acbf8d016fe0b2c8a4"
    ): Call<ApiResult>

}