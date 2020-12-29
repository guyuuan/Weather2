package cn.chitanda.weather.network

import android.util.Log
import cn.chitanda.weather.model.ApiResult
import cn.chitanda.weather.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *@auther: Chen
 *@createTime: 2020/12/29 21:00
 *@description:
 **/
private const val TAG = "NetworkManager"

class NetworkManager private constructor(private val onError: (String) -> Unit) {

    companion object {
        private var instance: NetworkManager? = null
        fun getInstance(onError: (String) -> Unit) =
            instance ?: synchronized(this) {
                instance ?: NetworkManager(onError).also { instance = it }
            }
    }

    private val api by lazy {
        RetrofitCreator.create(Api::class.java)
    }

    suspend fun getNow(location: String) = api.getNowWeather(location).await()


    suspend fun getCity(location: String) =
        api.getCity(location = location).await()

    suspend fun getHourly(location: String) =
        api.getHourly(location = location).await()

    suspend fun getDaily(location: String) = api.getDaily(location).await()
    suspend fun getWeather(location: String) = withContext(Dispatchers.IO) {
        val city = async { getCity(location) }
        val now = async { getNow(location) }
        val daily = async { getDaily(location) }
        val hourly = async { getHourly(location) }
        Weather(
            updateTime = System.currentTimeMillis(),
            location = city.await().locations?.first()
                ?: return@withContext null,
            now = now.await().now,
            daily = daily.await().daily,
            hourly = hourly.await().hourly
        )
    }

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    onError(t.message.toString())
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        Log.d(TAG, "${call.request().url} http response body is null")
                        onError(response.message())
                    }
                }
            })
        }
    }
}
