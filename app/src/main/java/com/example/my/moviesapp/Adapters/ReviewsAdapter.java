package com.example.my.moviesapp.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my.moviesapp.Models.Review;
import com.example.my.moviesapp.R;


public class ReviewsAdapter extends ArrayAdapter<Review> {

    Context context;

    public ReviewsAdapter(final Context context, final int layoutResourceId) {
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
            row = inflater.inflate(R.layout.review_row, parent, false);
            recordHolder.author_name = (TextView) row.findViewById(R.id.author_name);
            recordHolder.review_content = (TextView) row.findViewById(R.id.review_content);
            row.setTag(recordHolder);

        } else {
            recordHolder = (RecordHolder) row.getTag();
        }

        Review reviewItem =  getItem(position);
        String author = reviewItem.getAuthor();
        String content = reviewItem.getComment();

        recordHolder.author_name.setText(author);
        recordHolder.review_content.setText(content);


        return row;
    }


    static class RecordHolder {
        TextView author_name;
        TextView review_content;
    }
}
