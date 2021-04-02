package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category extends AppCompatActivity {

    private final String API_KEY = "9d88f633d38146cda348f2576024482c";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter newsAdapter;
    private List<Article> articleList = new ArrayList<>();
    private TextView categoryTop;
    private String categoryTopText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        categoryTop = findViewById(R.id.categoryTop);
        categoryTopText = getIntent().getStringExtra("text");
        categoryTop.setText(categoryTopText.toUpperCase());
        recyclerView = findViewById(R.id.recyclerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String country = getCountry();

        swipeRefreshLayout.setOnRefreshListener(() -> getJson(categoryTopText, country, API_KEY));

        getJson(categoryTopText, country, API_KEY);

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
                    newsAdapter = new NewsAdapter(Category.this, articleList);
                    recyclerView.setAdapter(newsAdapter);
                }
            }

            @Override
            public void onFailure(Call<Headline> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(Category.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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