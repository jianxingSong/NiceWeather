package com.example.niceweather.logic

import androidx.lifecycle.liveData
import com.example.niceweather.NiceWeatherApplication
import com.example.niceweather.logic.network.NiceWeatherNetwork
import kotlinx.coroutines.Dispatchers

// 仓库层：判断调用方请求的数据应该是从本地数据源获取还是从网络数据源获取，并将获得的数据返回给调用方
object Repository {
    //实现从网络中异步搜索地点（place）并将结果包装位LiveData
    // 这里直接从网络数据源获取，因为搜索城市数据没有太多缓存的必要
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