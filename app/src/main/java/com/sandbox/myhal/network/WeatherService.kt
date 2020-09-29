package com.sandbox.weatherapp.network

import com.sandbox.weatherapp.models.WeatherResponse
import retrofit.http.GET
import retrofit.http.Query
import retrofit.Call

interface WeatherService {
    @GET("2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appid: String?,
    ) : Call<WeatherResponse>
}