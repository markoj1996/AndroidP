package com.example.mj.projekat.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mj.projekat.R;
import com.example.mj.projekat.model.Comment;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.User;

import java.util.List;

public class commentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> mComList;
    private List<User> mUserList;

    public commentAdapter(Context mContext, List<Comment> mComList,List<User> mUserList) {
        this.mContext = mContext;
        this.mComList = mComList;
        this.mUserList = mUserList;
    }

    @Override
    public int getCount() {
        return mComList.size();
    }

    @Override
    public Object getItem(int position) {
        return mComList.get(position);
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
            view = inflater.inflate(R.layout.comment_layout, null);
        } else {
            view = convertView;
        }
        TextView com = (TextView) view.findViewById(R.id.comment);
        Button like = (Button) view.findViewById(R.id.likeBtn);
        Button dislike = (Button) view.findViewById(R.id.dislikeBtn);
        TextView userInfo = (TextView) view.findViewById(R.id.userInfo);
        ImageView img = (ImageView) view.findViewById(R.id.slikaKom);

        com.setText(mComList.get(position).getDescription());
        like.setText("Like: "+"("+mComList.get(position).getLikes()+")");
        dislike.setText("Dislike: "+"("+mComList.get(position).getDislikes()+")");
        for(User u : mUserList)
        {
            if(u.getUsername().equals(mComList.get(position).getAuthor()))
            {
                userInfo.setText(u.getUsername());
                img.setImageBitmap(u.getPhoto());
            }
        }


        return view;
    }

}
