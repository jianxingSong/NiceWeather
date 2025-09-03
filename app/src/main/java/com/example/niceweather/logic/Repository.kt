package com.example.niceweather.logic

import androidx.lifecycle.liveData
import com.example.niceweather.NiceWeatherApplication
import com.example.niceweather.logic.model.DailyResponse
import com.example.niceweather.logic.model.Weather
import com.example.niceweather.logic.network.NiceWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

// 仓库层：判断调用方请求的数据应该是从本地数据源获取还是从网络数据源获取，并将获得的数据返回给调用方
object Repository {
    //实现从网络中异步搜索地点（place）并将结果包装位LiveData
    // 这里直接从网络数据源获取，因为搜索城市数据没有太多缓存的必要
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
            val placeResponse = NiceWeatherNetwork.searchPlaces(query) //发起网络请求 挂起协程 直到结果返回
            if(placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places) //取出结果，包装成Result.success()
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                NiceWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                NiceWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if(realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}