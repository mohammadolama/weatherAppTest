package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.OpenWeatherMap;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView city , temperature,weatherCondition,humidity,maxTemp , minTemp , pressure , wind;
    private ImageView imageView;
    private Button search;
    TextInputLayout cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        city = findViewById(R.id.text_view_city_weather);
        temperature = findViewById(R.id.text_view_temp_weather);
        weatherCondition = findViewById(R.id.text_view_weather_condition_weather);
        humidity = findViewById(R.id.humidity_weather);
        maxTemp = findViewById(R.id.maxtemp_weather);
        minTemp = findViewById(R.id.mintemp_weather);
        pressure = findViewById(R.id.pressure_weather);
        wind = findViewById(R.id.wind_weather);
        imageView = findViewById(R.id.imageView_weather);
        search = findViewById(R.id.search);

        cityName = findViewById(R.id.city_input);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = cityName.getEditText().getText().toString();
                if (!name.equals("")){
                    getWeatherData(name);
                    cityName.getEditText().setText("");
                    hideSoftKeyboard();
//                    dispatchTouchEvent()
                }
            }
        });
    }


    public void getWeatherData(String name){
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if (response.isSuccessful()) {
                    city.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                    temperature.setText(response.body().getMain().getTemp() + " C");
                    weatherCondition.setText(response.body().getWeather().get(0).getDescription());
                    humidity.setText(" : " + response.body().getMain().getHumidity() + "%");
                    maxTemp.setText(" : " + response.body().getMain().getTempMax() + " C");
                    minTemp.setText(" : " + response.body().getMain().getTempMin() + " C");
                    pressure.setText(" : " + response.body().getMain().getPressure());
                    wind.setText(" : " + response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png").
                            placeholder(R.drawable.ic_launcher_background).into(imageView);

                }else {
                    Toast.makeText(WeatherActivity.this, "City not found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
    private void hideSoftKeyboard(){
        if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
        }
    }

}