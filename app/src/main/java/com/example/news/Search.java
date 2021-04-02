package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsAdapter;
import com.example.news.clients.NewsApi;
import com.example.news.helpers.BottomNavigationViewHelper;
import com.example.news.models.Article;
import com.example.news.models.Headline;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends AppCompatActivity {

    private final static String[] topics = {
            "Stocks",
            "USA",
            "Russia",
            "Ireland",
            "Politics",
            "Covid-19",
            "Entertainment",
            "Business",
            "Music",
            "Lifestyle",
            "Sports",
            "Technology",
            "Crypto"};
    private final String API_KEY = "9d88f633d38146cda348f2576024482c";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter newsAdapter;
    private List<Article> articleList = new ArrayList<>();
    private Button searchButton, searchMap;
    private EditText searchBar;

    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        recyclerView = findViewById(R.id.recyclerView);
        searchButton = findViewById(R.id.searchButton);
        searchBar = findViewById(R.id.searchBar);
        searchMap = findViewById(R.id.searchMap);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.search);

        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.categories:
                    startActivity(new Intent(getApplicationContext(), Categories.class));
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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String country = getCountry();
        swipeRefreshLayout.setOnRefreshListener(() -> getJson(getRandom(topics), country, API_KEY));

        getJson(getRandom(topics), country, API_KEY);

        searchButton.setOnClickListener(v -> {
            if (!searchBar.getText().toString().equals("")) {
                swipeRefreshLayout.setOnRefreshListener(() -> getJson(
                        searchBar.getText().toString(), country, API_KEY));
                getJson(searchBar.getText().toString(), country, API_KEY);
            } else {
                swipeRefreshLayout.setOnRefreshListener(() -> getJson(getRandom(topics), country, API_KEY));
                getJson(getRandom(topics), country, API_KEY);
            }
        });

        searchMap.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Maps.class)));

    }

    public void getJson(String query, String country, String apiKey) {

        swipeRefreshLayout.setRefreshing(true);
        Call<Headline> call = NewsApi.getInstance().getApi().getSearch(query, apiKey);

        call.enqueue(new Callback<Headline>() {
            @Override
            public void onResponse(Call<Headline> call, Response<Headline> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    articleList.clear();
                    articleList = response.body().getArticles();
                    newsAdapter = new NewsAdapter(Search.this, articleList);
                    recyclerView.setAdapter(newsAdapter);
                }
            }

            @Override
            public void onFailure(Call<Headline> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(Search.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        System.out.println(country);
        return country;
    }
}