package com.example.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.helpers.BottomNavigationViewHelper;
import com.example.news.models.Article;
import com.example.news.models.Source;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsPage extends AppCompatActivity {

    private static final String TAG = "";
    TextView titleTextView, sourceTextView, timeTextView;
    String title, source, date, url, imageUrl;
    WebView webView;
    ProgressBar progressBar;
    ImageView imageView;
    Button save;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_page);

        titleTextView = findViewById(R.id.title);
        sourceTextView = findViewById(R.id.source);
        timeTextView = findViewById(R.id.date);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        progressBar.setVisibility(View.VISIBLE);

        Intent i = getIntent();
        title = i.getStringExtra("title");
        source = i.getStringExtra("source");
        date = i.getStringExtra("date");
        url = i.getStringExtra("url");
        imageUrl = i.getStringExtra("imageUrl");

        titleTextView.setText(title);
        sourceTextView.setText(source);
        timeTextView.setText(dateTime(date));

        Picasso.with(NewsPage.this).load(imageUrl).fit().into(imageView);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        if (webView.isShown()) {
            progressBar.setVisibility(View.INVISIBLE);
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.invalidate();

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

        save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Article article = new Article(new Source(source, source), title, url, imageUrl, date, userId);
            FirebaseFirestore.getInstance()
                    .collection("articles")
                    .add(article)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(NewsPage.this, "Saved.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {

                    });
        });


    }

    public String dateTime(String time) {
        PrettyTime prettyTime = new PrettyTime(new Locale("EN"));
        String dateTime = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(time);
            dateTime = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }
}