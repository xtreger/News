package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.helpers.BottomNavigationViewHelper;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    private Button logout, viewSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        logout = findViewById(R.id.logout);
        viewSaved = findViewById(R.id.savedButton);

        logout.setOnClickListener(v -> {
            AuthUI.getInstance().signOut(Profile.this);
            Profile.this.finish();
            Intent loginIntent = new Intent(Profile.this, Login.class);
            startActivity(loginIntent);
        });


        viewSaved.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, SavedNews.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.profile);

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

}