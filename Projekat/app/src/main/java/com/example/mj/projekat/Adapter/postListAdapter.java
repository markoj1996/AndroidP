package com.example.mj.projekat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mj.projekat.R;
import com.example.mj.projekat.model.Post;

import java.util.List;

public class postListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Post> mPostsList;

    public postListAdapter(Context mContext, List<Post> mPostsList) {
        this.mContext = mContext;
        this.mPostsList = mPostsList;
    }

    @Override
    public int getCount() {
        return mPostsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPostsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customlayout, null);
        } else {
            view = convertView;
        }
        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        TextView name = (TextView) view.findViewById(R.id.textName);
        TextView desc = (TextView) view.findViewById(R.id.textDesc);

        image.setImageBitmap(mPostsList.get(position).getPhoto());
        name.setText(mPostsList.get(position).getTitle());
        desc.setText(mPostsList.get(position).getDescription());

        return view;
    }

}
