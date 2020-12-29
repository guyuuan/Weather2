package cn.chitanda.weather.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author:       Chen
 * @Date:         2020/12/29 13:16
 * @Email:        "chunjinchen1998@gmail.com"
 * @Description:
 */
object RetrofitCreator {
    private const val TAG = "RetrofitCreator"
    private const val BASE_URL = "https://devapi.qweather.com/"

    private val builder by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.i(TAG, message)
                    }
                }).apply { level = HttpLoggingInterceptor.Level.BODY })
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val retrofit by lazy {
        builder.build()
    }

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}