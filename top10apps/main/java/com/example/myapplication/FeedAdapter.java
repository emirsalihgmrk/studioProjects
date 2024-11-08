package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FeedAdapter<T extends FeedEntry> extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<T> applications;

    public FeedAdapter(@NonNull Context context, int resource, List<T> applications) {
        super(context, resource);
        this.applications = applications;
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            Log.d(TAG, "getView: called with null convertView");
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            Log.d(TAG, "getView: provided a convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        T currentApp = applications.get(position);

        viewHolder.creator.setText(currentApp.getDc_creator());
        viewHolder.pubDate.setText(currentApp.getPubDate());
        viewHolder.description.setText(currentApp.getDescription());

        return convertView;
    }

    private class ViewHolder{
        final TextView creator;
        final TextView pubDate;
        final TextView description;

        public ViewHolder(View v) {
            this.creator = v.findViewById(R.id.creator);
            this.pubDate = v.findViewById(R.id.pub_date);
            this.description = v.findViewById(R.id.description);
        }
    }
}
