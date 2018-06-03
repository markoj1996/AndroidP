package com.example.mj.projekat;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mj.projekat.Database.CommentDb;
import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.PostDb;

import java.util.Date;

public class CreateCommentActivity extends AppCompatActivity {

    EditText title;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        title = (EditText)findViewById(R.id.comTitle);
        desc = (EditText)findViewById(R.id.comDesc);
        Button create = (Button)findViewById(R.id.createCom);
        Button cancel = (Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myTitle = title.getText().toString();
                String myDesc = desc.getText().toString();
                SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
                String author = sharedPref.getString("userName","");
                Date datum = new Date();

                Bundle post = getIntent().getExtras();
                if(post==null)
                {
                    return;
                }

                final int postId = post.getInt("postId");

                switch (v.getId())
                {
                    case R.id.createCom:

                        // check for blanks
                        if(myTitle.trim().equalsIgnoreCase("")){
                            Toast.makeText(getBaseContext(), "Please ENTER title", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // check for blanks
                        if(myDesc.trim().equalsIgnoreCase("")){
                            Toast.makeText(getBaseContext(), "Please ENTER description", Toast.LENGTH_LONG).show();
                            return;
                        }

                        ContentValues values = new ContentValues();
                        values.put(CommentDb.KEY_TITLE, myTitle);
                        values.put(CommentDb.KEY_DESCRIPTION, myDesc);
                        values.put(CommentDb.KEY_AUTHOR,author);
                        values.put(CommentDb.KEY_DATE,datum.toString());
                        values.put(CommentDb.KEY_POST,postId);
                        values.put(PostDb.KEY_LIKES,0);
                        values.put(PostDb.KEY_DISLIKES,0);
                        getContentResolver().insert(MyContentProvider.CONTENT_URI4, values);
                        finish();
                        break;
                }

            }
        });

    }

}
