package com.example.mj.projekat.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mj.projekat.Database.CommentDb;
import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.PostDb;
import com.example.mj.projekat.PostsActivity;
import com.example.mj.projekat.R;
import com.example.mj.projekat.ReadPostActivity;
import com.example.mj.projekat.model.Comment;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.User;

import java.util.List;

public class commentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> mComList;
    private List<User> mUserList;
    private String loggedInUser;

    public commentAdapter(Context mContext, List<Comment> mComList,List<User> mUserList,String loggedInUser) {
        this.mContext = mContext;
        this.mComList = mComList;
        this.mUserList = mUserList;
        this.loggedInUser = loggedInUser;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;



        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.comment_layout, null);
        } else {
            view = convertView;
        }
        TextView com = (TextView) view.findViewById(R.id.comment);
        final Button like = (Button) view.findViewById(R.id.likeBtn);
        final Button dislike = (Button) view.findViewById(R.id.dislikeBtn);
        final Button delete = (Button) view.findViewById(R.id.delete);
        TextView userInfo = (TextView) view.findViewById(R.id.userInfo);
        ImageView img = (ImageView) view.findViewById(R.id.slikaKom);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedInUser.equals(mComList.get(position).getAuthor()))
                {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI4 + "/" + mComList.get(position).getId());
                    mContext.getContentResolver().delete(uri, null, null);
                }else {
                    Toast.makeText(mContext,"Ne mozete obrisati komentar koju niste napravili!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dislikeInt = Color.parseColor("#f00000");
                ColorDrawable cd = (ColorDrawable) dislike.getBackground();
                int dislikebtnInt = cd.getColor();
                if(loggedInUser.equals(mComList.get(position).getAuthor()))
                {

                }else
                {
                    if(dislikeInt==dislikebtnInt)
                    {
                        dislike.setTextColor(Color.parseColor("#f00000"));
                        dislike.setBackgroundColor(Color.parseColor("#DDDDDD"));
                        mComList.get(position).setDislikes(mComList.get(position).getDislikes()-1);
                        dislike.setText("Dislike: "+mComList.get(position).getDislikes());
                        ContentValues values = new ContentValues();
                        values.put(CommentDb.KEY_ROWID, mComList.get(position).getId());
                        values.put(CommentDb.KEY_TITLE, mComList.get(position).getTitle());
                        values.put(CommentDb.KEY_AUTHOR, mComList.get(position).getAuthor());
                        values.put(CommentDb.KEY_DATE, mComList.get(position).getDate());
                        values.put(CommentDb.KEY_POST, mComList.get(position).getPost());
                        values.put(CommentDb.KEY_LIKES, mComList.get(position).getLikes());
                        values.put(CommentDb.KEY_DISLIKES, mComList.get(position).getDislikes());
                        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI4 + "/" + mComList.get(position).getId());
                        mContext.getContentResolver().update(uri, values, null, null);
                    }
                    mComList.get(position).setLikes(mComList.get(position).getLikes()+1);
                    like.setText("Like: "+mComList.get(position).getLikes());
                    like.setBackgroundColor(Color.parseColor("#0000ff"));
                    like.setTextColor(Color.parseColor("#FFFFFF"));
                    ContentValues values = new ContentValues();
                    values.put(CommentDb.KEY_ROWID, mComList.get(position).getId());
                    values.put(CommentDb.KEY_TITLE, mComList.get(position).getTitle());
                    values.put(CommentDb.KEY_AUTHOR, mComList.get(position).getAuthor());
                    values.put(CommentDb.KEY_DATE, mComList.get(position).getDate());
                    values.put(CommentDb.KEY_POST, mComList.get(position).getPost());
                    values.put(CommentDb.KEY_LIKES, mComList.get(position).getLikes());
                    values.put(CommentDb.KEY_DISLIKES, mComList.get(position).getDislikes());
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI4 + "/" + mComList.get(position).getId());
                    mContext.getContentResolver().update(uri, values, null, null);
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeInt = Color.parseColor("#0000ff");
                ColorDrawable cd = (ColorDrawable) like.getBackground();
                int likebtnInt = cd.getColor();

                if(loggedInUser.equals(mComList.get(position).getAuthor()))
                {

                }else {
                    if (likeInt == likebtnInt) {
                        like.setTextColor(Color.parseColor("#0000ff"));
                        like.setBackgroundColor(Color.parseColor("#DDDDDD"));
                        mComList.get(position).setLikes(mComList.get(position).getLikes() - 1);
                        like.setText("Like: " + mComList.get(position).getLikes());
                        ContentValues values = new ContentValues();
                        values.put(CommentDb.KEY_ROWID, mComList.get(position).getId());
                        values.put(CommentDb.KEY_TITLE, mComList.get(position).getTitle());
                        values.put(CommentDb.KEY_AUTHOR, mComList.get(position).getAuthor());
                        values.put(CommentDb.KEY_DATE, mComList.get(position).getDate());
                        values.put(CommentDb.KEY_POST, mComList.get(position).getPost());
                        values.put(CommentDb.KEY_LIKES, mComList.get(position).getLikes());
                        values.put(CommentDb.KEY_DISLIKES, mComList.get(position).getDislikes());
                        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI4 + "/" + mComList.get(position).getId());
                        mContext.getContentResolver().update(uri, values, null, null);
                    }
                    mComList.get(position).setDislikes(mComList.get(position).getDislikes() + 1);
                    dislike.setText("Dislike: " + mComList.get(position).getDislikes());
                    dislike.setBackgroundColor(Color.parseColor("#F00000"));
                    dislike.setTextColor(Color.parseColor("#FFFFFF"));
                    ContentValues values = new ContentValues();
                    values.put(CommentDb.KEY_ROWID, mComList.get(position).getId());
                    values.put(CommentDb.KEY_TITLE, mComList.get(position).getTitle());
                    values.put(CommentDb.KEY_AUTHOR, mComList.get(position).getAuthor());
                    values.put(CommentDb.KEY_DATE, mComList.get(position).getDate());
                    values.put(CommentDb.KEY_POST, mComList.get(position).getPost());
                    values.put(CommentDb.KEY_LIKES, mComList.get(position).getLikes());
                    values.put(CommentDb.KEY_DISLIKES, mComList.get(position).getDislikes());
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI4 + "/" + mComList.get(position).getId());
                    mContext.getContentResolver().update(uri, values, null, null);
                }
            }
        });

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
