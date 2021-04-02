package com.example.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.NewsPage;
import com.example.news.R;
import com.example.news.models.Article;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Context context;
    List<Article> articleList;

    public NewsAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article article = articleList.get(position);
        holder.title.setText(article.getTitle());
        holder.source.setText(article.getSource().getName());
        holder.date.setText(dateTime(article.getPublishedAt()));


        String imageUrl = article.getUrlToImage();

        Picasso.with(context).load(imageUrl).into(holder.image);

        holder.cardView.setOnClickListener(v -> {

            Intent i = new Intent(context, NewsPage.class);
            i.putExtra("title", article.getTitle());
            i.putExtra("source", article.getSource().getName());
            i.putExtra("date", article.getPublishedAt());
            i.putExtra("url", article.getUrl());
            i.putExtra("imageUrl", article.getUrlToImage());

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public String dateTime(String time) {
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
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

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, source, date;
        ImageView image;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            source = itemView.findViewById(R.id.source);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }

}
