package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsAdapter;
import com.example.news.helpers.BottomNavigationViewHelper;
import com.example.news.models.Article;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SavedNews extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsAdapter newsAdapter;
    private List<Article> articleList = new ArrayList<>();
    private FirebaseUser currentUser;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_news);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, Login.class));
            this.finish();
        } else {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
        }


        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        recyclerView = findViewById(R.id.recyclerView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            switch (item.getItemId()) {
                case R.id.categories:
                    startActivity(new Intent(getApplicationContext(), Categories.class));
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
                case R.id.trending:
                    startActivity(new Intent(getApplicationContext(), Trending.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        swipeRefreshLayout.setOnRefreshListener(this::getArticles);

        getArticles();

    }


    public void getArticles() {
        FirebaseFirestore.getInstance().collection("articles").whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    articleList.clear();
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        article = ds.toObject(Article.class);
                        articleList.add(article);
                    }

                    newsAdapter = new NewsAdapter(SavedNews.this, articleList);
                    recyclerView.setAdapter(newsAdapter);

                });


    }

}