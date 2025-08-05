package com.example.niceweather.logic

import androidx.lifecycle.liveData
import com.example.niceweather.NiceWeatherApplication
import com.example.niceweather.logic.network.NiceWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    //实现从网络中异步搜索地点（place）并将结果包装位LiveData
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = NiceWeatherNetwork.searchPlaces(query) //发起网络请求 挂起协程 直到结果返回
            if(placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places) //取出结果，包装成Result.success()
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result) //将result发射给所有观察者
    }
}