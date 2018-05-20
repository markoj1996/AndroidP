package com.example.mj.projekat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mj.projekat.Adapter.postListAdapter;
import com.example.mj.projekat.model.Comment;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.Tag;
import com.example.mj.projekat.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView lvPosts;
    private postListAdapter adapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
        String userName = sharedPref.getString("userName","");

        Bundle post = getIntent().getExtras();
        if(post==null)
        {
            return;
        }

        byte imageInByte[] = post.getByteArray("photoinbyte");
        Bitmap image = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);

        lvPosts = (ListView)findViewById(R.id.listView);
        posts = new ArrayList<>();


        Bitmap b = Bitmap.createBitmap(300, 200, Bitmap.Config.ALPHA_8);
        b.eraseColor(Color.GREEN);

        posts.add(new Post(1,"Post1","gdaaasf",b,100,1));

        adapter = new postListAdapter(getApplicationContext(),posts);

        lvPosts.setAdapter(adapter);

        lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post =(Post) parent.getItemAtPosition(position);
                //Toast.makeText(PostsActivity.this,post.getTitle(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PostsActivity.this,ReadPostActivity.class);
                i.putExtra("title",post.getTitle());
                i.putExtra("desc",post.getDescription());
                i.putExtra("like",post.getLikes());
                i.putExtra("dislike",post.getDislikes());
                i.putExtra("photo",post.getPhoto());
                startActivity(i);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Posts");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.loggedInUser);
        nav_user.setText(userName);
        ImageView profile = (ImageView)hView.findViewById(R.id.avatar);
        profile.setImageBitmap(image);

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
                                Intent i = new Intent(PostsActivity.this, PostsActivity.class);
                                startActivity(i);
                                return true;
                            case R.id.settings:
                                Intent ii = new Intent(PostsActivity.this, SettingsActivity.class);
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
