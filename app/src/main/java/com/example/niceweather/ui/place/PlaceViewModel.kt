package com.example.niceweather.ui.place

import android.view.animation.Transformation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.niceweather.logic.Repository
import com.example.niceweather.logic.model.Place
import retrofit2.http.Query

class PlaceViewModel: ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    // Activity观察这个LiveData，而每次searchLiveData变化的税后，就会调用Repository.searchPlaces()来将结果返回给placeLiveData
    val placeLiveData = searchLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}