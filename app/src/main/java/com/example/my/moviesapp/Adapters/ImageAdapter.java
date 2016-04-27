package com.example.my.moviesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.my.moviesapp.Constants.Constants;
import com.example.my.moviesapp.Models.Movie;
import com.example.my.moviesapp.R;
import com.squareup.picasso.Picasso;


public class ImageAdapter extends ArrayAdapter<Movie> {

    Context context;

    public ImageAdapter(final Context context, final int layoutResourceId) {
        super(context, layoutResourceId);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        RecordHolder recordHolder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            recordHolder = new RecordHolder();
            row = inflater.inflate(R.layout.content_gridview, parent, false);
            recordHolder.movieImage = (ImageView) row.findViewById(R.id.movieImage);
            row.setTag(recordHolder);

        } else {
            recordHolder = (RecordHolder) row.getTag();
        }

        Movie movieItem =  getItem(position);
        String posterPath = movieItem.getPosterPath();
        Picasso.with(context).load(Constants.BASE_IMAGE_URL+posterPath).into(recordHolder.movieImage);

        return row;
    }


    static class RecordHolder {
        ImageView movieImage;
    }
}
