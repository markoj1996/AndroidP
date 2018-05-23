package com.example.mj.projekat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.PostDb;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.User;

public class ReadPostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
        final String loggedInUser = sharedPref.getString("userName","");

        final Bundle post = getIntent().getExtras();
        if(post==null)
        {
            return;
        }
        int id = post.getInt("id");
        String naslov = post.getString("title");
        final TextView title = (TextView)findViewById(R.id.titleTV);
        title.setText(naslov);
        String autor = post.getString("autor");
        String datum = post.getString("datum");
        String location = post.getString("location");
        final TextView info = (TextView)findViewById(R.id.info);
        info.setText("Autor: "+autor+"  Datum: "+datum);
        String desc = post.getString("desc");
        final Button like = (Button)findViewById(R.id.likeButton);
        final Button dislike = (Button)findViewById(R.id.dislikeButton);
        final int likeP = post.getInt("likes");
        final int dislikeP = post.getInt("dislikes");
        like.setText("Like: "+"("+likeP+")");
        dislike.setText("Dislike: "+"("+dislikeP+")");
        final TextView descr = (TextView)findViewById(R.id.sadrzaj);
        descr.setText(desc);
        final byte imageInByte[] = post.getByteArray("photoinbyte");
        Bitmap image = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);
        final ImageView img = (ImageView)findViewById(R.id.slika);
        img.setImageBitmap(image);

        final Post p = new Post();
        p.setId(id);
        p.setTitle(naslov);
        p.setDescription(desc);
        p.setAuthor(autor);
        p.setDate(datum);
        p.setLocation(location);
        p.setPhoto(image);
        p.setLikes(likeP);
        p.setDislikes(dislikeP);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dislikeInt = Color.parseColor("#f00000");
                ColorDrawable cd = (ColorDrawable) dislike.getBackground();
                int dislikebtnInt = cd.getColor();
                if(loggedInUser.equals(p.getAuthor()))
                {
                    Toast.makeText(ReadPostActivity.this,"Ne mozete da lajkujete svoju objavu",Toast.LENGTH_SHORT).show();
                }else
                    {
                        if(dislikeInt==dislikebtnInt)
                        {
                            dislike.setTextColor(Color.parseColor("#f00000"));
                            dislike.setBackgroundColor(Color.parseColor("#DDDDDD"));
                            p.setDislikes(p.getDislikes()-1);
                            dislike.setText("Dislike: "+p.getDislikes());
                            ContentValues values = new ContentValues();
                            values.put(PostDb.KEY_ROWID, p.getId());
                            values.put(PostDb.KEY_TITLE, p.getTitle());
                            values.put(PostDb.KEY_DESCRIPTION, p.getDescription());
                            values.put(PostDb.KEY_PHOTO, imageInByte);
                            values.put(PostDb.KEY_AUTHOR, p.getAuthor());
                            values.put(PostDb.KEY_DATE, p.getDate());
                            values.put(PostDb.KEY_LOCATION, p.getLocation());
                            values.put(PostDb.KEY_DISLIKES, p.getDislikes());
                            values.put(PostDb.KEY_LIKES, p.getLikes());
                            Uri uri = Uri.parse(MyContentProvider.CONTENT_URI2 + "/" + p.getId());
                            getContentResolver().update(uri, values, null, null);
                        }
                        p.setLikes(p.getLikes()+1);
                        like.setText("Like: "+p.getLikes());
                        like.setBackgroundColor(Color.parseColor("#0000ff"));
                        like.setTextColor(Color.parseColor("#FFFFFF"));
                        ContentValues values = new ContentValues();
                        values.put(PostDb.KEY_ROWID, p.getId());
                        values.put(PostDb.KEY_TITLE, p.getTitle());
                        values.put(PostDb.KEY_DESCRIPTION, p.getDescription());
                        values.put(PostDb.KEY_PHOTO, imageInByte);
                        values.put(PostDb.KEY_AUTHOR, p.getAuthor());
                        values.put(PostDb.KEY_DATE, p.getDate());
                        values.put(PostDb.KEY_LOCATION, p.getLocation());
                        values.put(PostDb.KEY_DISLIKES, p.getDislikes());
                        values.put(PostDb.KEY_LIKES, p.getLikes());
                        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI2 + "/" + p.getId());
                        getContentResolver().update(uri, values, null, null);
                    }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likeInt = Color.parseColor("#0000ff");
                ColorDrawable cd = (ColorDrawable) like.getBackground();
                int likebtnInt = cd.getColor();

                if(loggedInUser.equals(p.getAuthor()))
                {
                    Toast.makeText(ReadPostActivity.this,"Ne mozete da dislajkujete svoju objavu",Toast.LENGTH_SHORT).show();
                }else
                {
                    if(likeInt==likebtnInt)
                    {
                        like.setTextColor(Color.parseColor("#0000ff"));
                        like.setBackgroundColor(Color.parseColor("#DDDDDD"));
                        p.setLikes(p.getLikes()-1);
                        like.setText("Like: "+p.getLikes());
                        ContentValues values = new ContentValues();
                        values.put(PostDb.KEY_ROWID, p.getId());
                        values.put(PostDb.KEY_TITLE, p.getTitle());
                        values.put(PostDb.KEY_DESCRIPTION, p.getDescription());
                        values.put(PostDb.KEY_PHOTO, imageInByte);
                        values.put(PostDb.KEY_AUTHOR, p.getAuthor());
                        values.put(PostDb.KEY_DATE, p.getDate());
                        values.put(PostDb.KEY_LOCATION, p.getLocation());
                        values.put(PostDb.KEY_DISLIKES, p.getDislikes());
                        values.put(PostDb.KEY_LIKES, p.getLikes());
                        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI2 + "/" + p.getId());
                        getContentResolver().update(uri, values, null, null);
                    }
                    p.setDislikes(p.getDislikes()+1);
                    dislike.setText("Dislike: "+p.getDislikes());
                    dislike.setBackgroundColor(Color.parseColor("#F00000"));
                    dislike.setTextColor(Color.parseColor("#FFFFFF"));
                    ContentValues values = new ContentValues();
                    values.put(PostDb.KEY_ROWID, p.getId());
                    values.put(PostDb.KEY_TITLE, p.getTitle());
                    values.put(PostDb.KEY_DESCRIPTION, p.getDescription());
                    values.put(PostDb.KEY_PHOTO, imageInByte);
                    values.put(PostDb.KEY_AUTHOR, p.getAuthor());
                    values.put(PostDb.KEY_DATE, p.getDate());
                    values.put(PostDb.KEY_LOCATION, p.getLocation());
                    values.put(PostDb.KEY_DISLIKES, p.getDislikes());
                    values.put(PostDb.KEY_LIKES, p.getLikes());
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI2 + "/" + p.getId());
                    getContentResolver().update(uri, values, null, null);

                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Read post");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        // set item as selected to persist highlight
                        //item.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (item.getItemId()) {
                            case R.id.posts:
                                Intent i = new Intent(ReadPostActivity.this,PostsActivity.class);
                                startActivity(i);
                                return true;
                            case R.id.settings:
                                Intent ii = new Intent(ReadPostActivity.this, SettingsActivity.class);
                                startActivity(ii);
                                return true;
                        }

                        return true;
                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
