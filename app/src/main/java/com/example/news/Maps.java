package com.example.news;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.news.helpers.BottomNavigationViewHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    LatLng selectedPosition;
    Button button;
    private GoogleMap mMap;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = findViewById(R.id.getNews);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng dublin = new LatLng(53.3497645, -6.2602732);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dublin));

        mMap.setOnMapClickListener(latLng -> {
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(latLng.latitude + " : " + latLng.longitude);
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            mMap.addMarker(markerOptions);
        });

        button.setOnClickListener(v -> {
            if (markerOptions != null) {
                Intent intent = new Intent(getApplicationContext(), CountryNews.class);
                selectedPosition = markerOptions.getPosition();
                String code = getCountry(selectedPosition.latitude, selectedPosition.longitude);
                intent.putExtra("text", code);
                startActivity(intent);
            } else
                Toast.makeText(this, "You have to select a location", Toast.LENGTH_SHORT).show();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.search);

        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.categories:
                    startActivity(new Intent(getApplicationContext(), Categories.class));
                    overridePendingTransition(0, 0);
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), Search.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.trending:
                    startActivity(new Intent(getApplicationContext(), Trending.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.weather:
                    startActivity(new Intent(getApplicationContext(), Weather.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    overridePendingTransition(0, 0);
            }
            return false;
        });
    }

    public String getCountry(Double lat, Double lon) {
        String placeName = "";
        try {
            Geocoder geo = new Geocoder(Maps.this, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lat, lon, 1);
            System.out.println(Geocoder.isPresent());
            if (addresses.size() > 0) {
                placeName = (addresses.get(0).getCountryCode());
            }

        } catch (Exception e) {
            Toast.makeText(this, "No Location Name Found", Toast.LENGTH_SHORT).show();
        }
        return placeName;
    }
}