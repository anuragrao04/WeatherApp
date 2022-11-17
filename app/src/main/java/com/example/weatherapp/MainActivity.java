package com.example.weatherapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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


import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "c9d36b91db136d84f018293af456fc77";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
    }


    public void getWeatherDetails(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        String tempurl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("")) {
            tvResult.setText("City field cannot be empty!");
        } else {
            if (!country.equals("")) {
                tempurl = url + "?q=" + city + "," + country + "&appid=" + appid + "&units=metric";
            } else {
                tempurl = url + "?q=" + city + "&appid=" + appid + "&units=metric";
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonObj = new JSONObject(response).getJSONArray("list").getJSONObject(0);

                        JSONObject main = jsonObj.getJSONObject("main");
                        JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                        JSONObject wind = jsonObj.getJSONObject("wind");
                        JSONObject sys = jsonObj.getJSONObject("sys");

                        String temperature = main.getString("temp");
                        String cast = weather.getString("description");
                        String humidity = main.getString("humidity");

                        String pre = main.getString("pressure");
                        String windspeed = wind.getString("speed");

                        tvResult.setTextColor(Color.BLACK);
                        tvResult.setTextSize(20);
                        output += "Current weather:"
                                + "\n Temp: " + temperature + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + cast
                                + "\n Wind Speed: " + windspeed + "m/s"
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