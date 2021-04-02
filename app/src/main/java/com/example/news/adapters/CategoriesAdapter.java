package com.example.news.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news.R;

public class CategoriesAdapter extends BaseAdapter {

    private final String[] headings;
    private final int[] images;
    private Context context;
    private LayoutInflater layoutInflater;

    public CategoriesAdapter(Context context, String[] headings, int[] images) {
        this.context = context;
        this.headings = headings;
        this.images = images;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.row_item, null);

        ImageView imageView = convertView.findViewById(R.id.imageCategory);
        TextView textView = convertView.findViewById(R.id.heading);
        imageView.setImageResource(images[position]);
        textView.setText(headings[position]);
        return convertView;
    }
}
