package com.example.arnold.itsosgadda.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arnold.itsosgadda.R;
import com.example.arnold.itsosgadda.post.PostData;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<PostData> {
    private final LayoutInflater inflater;
    private final ArrayList<PostData> data;

    public PostAdapter(Context context, int textViewResourceId,
                       ArrayList<PostData> objects) {
        super(context, textViewResourceId, objects);
        inflater = ((Activity) context).getLayoutInflater();
        data = objects;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_article, null);

            viewHolder = new ViewHolder();
            viewHolder.postThumbView = (ImageView) convertView
                    .findViewById(R.id.postThumb);
            viewHolder.postTitleView = (TextView) convertView
                    .findViewById(R.id.postTitleLabel);
            viewHolder.postDateView = (TextView) convertView
                    .findViewById(R.id.postDateLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (data.get(position).postThumbUrl == null) {
            viewHolder.postThumbView
                    .setImageResource(R.drawable.muslim_logo_creatorb);
        }

        viewHolder.postTitleView.setText(data.get(position).postTitle);
        viewHolder.postDateView.setText(data.get(position).postDate);

        return convertView;
    }

    static class ViewHolder {
        TextView postTitleView;
        TextView postDateView;
        ImageView postThumbView;
    }
}
