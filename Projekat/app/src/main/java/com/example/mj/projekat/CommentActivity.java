package com.example.mj.projekat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.mj.projekat.Adapter.commentAdapter;
import com.example.mj.projekat.Database.CommentDb;
import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.UsersDb;
import com.example.mj.projekat.model.Comment;
import com.example.mj.projekat.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    List<Comment> komentari = new ArrayList<>();
    List<User> users = new ArrayList<>();
    commentAdapter adapter;
    Button addCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        addCom = (Button)findViewById(R.id.addCom);

        ListView lista = (ListView)findViewById(R.id.listaKom);

        Bundle post = getIntent().getExtras();
        if(post==null)
        {
            return;
        }

        final int postId = post.getInt("postId");

        Cursor cc = getContentResolver().query(MyContentProvider.CONTENT_URI4,null,null,null,null);

        if(cc.moveToFirst())
        {
            do{
                Comment com = new Comment();
                com.setId(cc.getInt(cc.getColumnIndex(CommentDb.KEY_ROWID)));
                com.setTitle(cc.getString(cc.getColumnIndex(CommentDb.KEY_TITLE)));
                com.setDescription(cc.getString(cc.getColumnIndex(CommentDb.KEY_DESCRIPTION)));
                com.setAuthor(cc.getString(cc.getColumnIndex(CommentDb.KEY_AUTHOR)));
                com.setDate(cc.getString(cc.getColumnIndex(CommentDb.KEY_DATE)));
                com.setPost(cc.getInt(cc.getColumnIndex(CommentDb.KEY_POST)));
                com.setLikes(cc.getInt(cc.getColumnIndex(CommentDb.KEY_LIKES)));
                com.setDislikes(cc.getInt(cc.getColumnIndex(CommentDb.KEY_DISLIKES)));

                if(com.getPost()==postId)
                {
                    komentari.add(com);
                }
            }while (cc.moveToNext());
        }

        Cursor cu = getContentResolver().query(MyContentProvider.CONTENT_URI,null,null,null,null);

        if(cu.moveToFirst())
        {
            do{
                User u = new User();
                u.setId(cu.getColumnIndex(UsersDb.KEY_ROWID));
                u.setName(cu.getString(cu.getColumnIndex(UsersDb.KEY_NAME)));
                u.setUsername(cu.getString(cu.getColumnIndex(UsersDb.KEY_USERNAME)));
                u.setPassword(cu.getString(cu.getColumnIndex(UsersDb.KEY_PASSWORD)));
                byte PhotoInByte[] = cu.getBlob(2);
                Bitmap sl = BitmapFactory.decodeByteArray(PhotoInByte,0,PhotoInByte.length);
                u.setPhoto(sl);
                users.add(u);
            }while (cu.moveToNext());
        }

        adapter = new commentAdapter(getApplicationContext(),komentari,users);
        lista.setAdapter(adapter);

        addCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CommentActivity.this,CreateCommentActivity.class);
                i.putExtra("postId",postId);
                startActivity(i);
            }
        });

    }
}
