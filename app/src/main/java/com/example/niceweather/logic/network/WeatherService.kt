package com.example.niceweather.logic.network

import com.example.niceweather.NiceWeatherApplication
import com.example.niceweather.logic.model.DailyResponse
import com.example.niceweather.logic.model.RealTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${NiceWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealTimeResponse>

    @GET("v2.5/${NiceWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse> //@Path表示用该变量替换URL中的占位符
}