package com.example.news;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.news.helpers.BottomNavigationViewHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Weather extends AppCompatActivity {

    private static final String WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String WEATHER_MAP_API = "417f5bde07a791b753f8eb333dda78a3";
    static String latitude;
    static String longitude;
    TextView cityTV, detailsTV, temperatureTV, humidityTV, pressureTV, iconTV, updatedTV;
    Typeface weatherFont;

    FusedLocationProviderClient fusedLocationProviderClient;

    public static String setWeatherIcon(int id1, long sunrise, long sunset) {
        int id = id1 / 100;
        String icon = "";
        if (id1 == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch (id) {
                case 2:
                    icon = "&#xf01e;";
                    break;
                case 3:
                    icon = "&#xf01c;";
                    break;
                case 5:
                    icon = "&#xf019;";
                    break;
                case 6:
                    icon = "&#xf01b;";
                    break;
                case 7:
                    icon = "&#xf014";
                    break;
                case 8:
                    icon = "&#xf013;";
                    break;
            }
        }
        return icon;
    }

    public static JSONObject getWeatherJSON(String latitude, String longitude) {
        try {
            URL url = new URL(String.format(WEATHER_MAP_URL, latitude, longitude));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key", WEATHER_MAP_API);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                json.append(tmp);
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());

            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;

        } catch (Exception e) {
            Log.d("Error", "Cannot get weather JSON");
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        requestPermissions();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Weather.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(Weather.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                    weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericonsfont.ttf");

                    cityTV = findViewById(R.id.city);
                    temperatureTV = findViewById(R.id.currentTemperature);
                    updatedTV = findViewById(R.id.updated);
                    detailsTV = findViewById(R.id.details);
                    humidityTV = findViewById(R.id.humidity);
                    pressureTV = findViewById(R.id.pressure);

                    String[] jsonData = getJSonResponse();

                    cityTV.setText(jsonData[0]);
                    detailsTV.setText(jsonData[1]);
                    temperatureTV.setText(jsonData[2]);
                    humidityTV.setText("Humidity: " + jsonData[3]);
                    pressureTV.setText("Pressure: " + jsonData[4]);
                    updatedTV.setText(jsonData[5]);


                }
            }
        });


        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.weather);

        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.trending:
                    startActivity(new Intent(getApplicationContext(), Trending.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), Search.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.categories:
                    startActivity(new Intent(getApplicationContext(), Categories.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    overridePendingTransition(0, 0);
            }
            return false;
        });

    }

    private String[] getJSonResponse() {
        String[] jsonData = new String[7];
        JSONObject jsonObject = null;

        try {
            jsonObject = getWeatherJSON(latitude, longitude);
        } catch (Exception e) {
            Log.d("Error", "Cannot parse JSON");
        }

        try {
            if (jsonObject != null) {
                JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonObject.getJSONObject("main");
                DateFormat dateFormat = DateFormat.getDateInstance();

                String city = jsonObject.getString("name") + ", " + jsonObject.getJSONObject("sys").getString("country");
                String description = details.getString("description").toLowerCase(Locale.ENGLISH);
                String temperature = String.format("%.0f", main.getDouble("temp")) + "°";
                String humidity = main.getString("humidity") + "%";
                String pressure = main.getString("pressure") + "hPa";
                String updated = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
                String icon = setWeatherIcon(details.getInt("id"), jsonObject
                                .getJSONObject("sys")
                                .getLong("sunrise") * 1000,
                        jsonObject.getJSONObject("sys")
                                .getLong("sunset") * 1000);

                jsonData[0] = city;
                jsonData[1] = description;
                jsonData[2] = temperature;
                jsonData[3] = humidity;
                jsonData[4] = pressure;
                jsonData[5] = updated;
                jsonData[6] = icon;
            }
        } catch (Exception e) {

        }
        return jsonData;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

}