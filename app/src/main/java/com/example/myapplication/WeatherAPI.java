package com.example.myapplication;

import com.example.myapplication.Model.OpenWeatherMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=5d948a7a73fdba8d050fab2ac08472a6&units=metric")
    Call<OpenWeatherMap> getWeatherWithLocation(@Query("lat") double lat , @Query("lon") double lon);

    @GET("weather?appid=5d948a7a73fdba8d050fab2ac08472a6&units=metric")
    Call<OpenWeatherMap> getWeatherWithCityName(@Query("q") String name );
}
