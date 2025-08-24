package com.example.niceweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// 可以在应用启动的时候进行一些全局初始化操作
class NiceWeatherApplication: Application() {
    companion object {
        //彩云天气令牌
        const val TOKEN = "mN2d3hkdiTyH3Vjd"

        @SuppressLint("StaticFieldLeak") //全局context
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}