package com.example.my.moviesapp.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my.moviesapp.R;

public class TrailersAdapter extends ArrayAdapter<String> {

    Context context;

    public TrailersAdapter(final Context context, final int layoutResourceId) {
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
            row = inflater.inflate(R.layout.trailer_row, parent, false);
            recordHolder.trailerText = (TextView) row.findViewById(R.id.trailerText);
            row.setTag(recordHolder);

        } else {
            recordHolder = (RecordHolder) row.getTag();
        }

        //String trailerItem =  getItem(position);
        recordHolder.trailerText .setText("Trailer " + (position+1));

        return row;
    }


    static class RecordHolder {
        TextView trailerText;
    }

}
