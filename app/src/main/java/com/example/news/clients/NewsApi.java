package com.example.news.clients;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApi {
    private static final String URL = "https://newsapi.org/v2/";
    private static NewsApi newsApi;
    private static Retrofit retrofit;

    private NewsApi() {
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized NewsApi getInstance() {
        if (newsApi == null) {
            newsApi = new NewsApi();
        }
        return newsApi;
    }

    public static NewsApiInterface getApi() {
        return retrofit.create(NewsApiInterface.class);
    }
}
