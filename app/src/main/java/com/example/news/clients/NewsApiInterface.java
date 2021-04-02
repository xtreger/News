package com.example.news.clients;

import com.example.news.models.Headline;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {

    @GET("top-headlines")
    Call<Headline> getHeadline(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<Headline> getEverything(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<Headline> getSearch(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );
}
