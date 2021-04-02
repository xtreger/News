package com.example.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.adapters.CategoriesAdapter;
import com.example.news.helpers.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Categories extends AppCompatActivity {
    private final static String[] headings = {
            "Stocks",
            "Politics",
            "Covid-19",
            "Entertainment",
            "Business",
            "Music",
            "Lifestyle",
            "Sports",
            "Technology",
            "Crypto"};
    private final static int[] images = {
            R.drawable.stocks,
            R.drawable.politics,
            R.drawable.covid,
            R.drawable.entertainment,
            R.drawable.business,
            R.drawable.music,
            R.drawable.lifestyle,
            R.drawable.sports,
            R.drawable.technology,
            R.drawable.crypto};


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        GridView gridView = findViewById(R.id.grid_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(Categories.this, headings, images);
        gridView.setAdapter(categoriesAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), Category.class);
            intent.putExtra("text", headings[position]);
            startActivity(intent);
        });

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.categories);

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