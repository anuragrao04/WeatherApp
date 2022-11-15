package com.example.weatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "c9d36b91db136d84f018293af456fc77";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String tempurl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("")) {
            tvResult.setText("City field cannot be empty!");
        } else {
            if (!country.equals("")) {
                tempurl = url + "?q=" + city + "," + country + "&appid=" + appid;
            } else {
                tempurl = url + "?q=" + city + "&appid=" + appid;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        JSONObject main = jsonObj.getJSONObject("main");
                        JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                        JSONObject wind = jsonObj.getJSONObject("wind");
                        JSONObject sys = jsonObj.getJSONObject("sys");


                        String city_name = jsonObj.getString("name");
                        String countryname = sys.getString("country");
                        String temperature = main.getString("temp");
                        String cast = weather.getString("description");
                        String humidity = main.getString("humidity");

                        String pre = main.getString("pressure");
                        String windspeed = wind.getString("speed");
                        Long rise = sys.getLong("sunrise");
                        String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));
                        Long set = sys.getLong("sunset");
                        String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                        tvResult.setTextColor(Color.rgb(68, 134, 199));
                        output += "Current weather of " + city_name + " (" + countryname + ")"
                                + "\n Temp: " + df.format(temperature) + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + cast
                                + "\n Wind Speed: " + windspeed + "m/s (meters per second)"
                                + "\n Sunrise: " + sunrise + "%"
                                + "\n Sunset: " + sunset + "%"
                                + "\n Pressure: " + pre + " hPa";
                        tvResult.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                Log.d(
                        "MainActivity",error.toString()
                );
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
}