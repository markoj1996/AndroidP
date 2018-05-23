package com.example.mj.projekat;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.mj.projekat.Adapter.postListAdapter;
import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.PostDb;
import com.example.mj.projekat.Database.UsersDb;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private SimpleCursorAdapter dataAdapter;


    private DrawerLayout mDrawerLayout;
    private ListView lvPosts;
    private postListAdapter adapter;
    private List<Post> posts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        Cursor c = getContentResolver().query(MyContentProvider.CONTENT_URI2,null,null,null,null);

        if(c.moveToFirst())
        {
            do{
                Post p = new Post();
                p.setId(c.getInt(c.getColumnIndex(PostDb.KEY_ROWID)));
                p.setTitle(c.getString(c.getColumnIndex(PostDb.KEY_TITLE)));
                p.setDescription(c.getString(c.getColumnIndex(PostDb.KEY_DESCRIPTION)));
                byte PostImageInByte[] = c.getBlob(3);
                Bitmap Pimage = BitmapFactory.decodeByteArray(PostImageInByte,0,PostImageInByte.length);
                p.setPhoto(Pimage);
                p.setAuthor(c.getString(c.getColumnIndex(PostDb.KEY_AUTHOR)));
                p.setDate(c.getString(c.getColumnIndex(PostDb.KEY_DATE)));
                p.setLocation(c.getString(c.getColumnIndex(PostDb.KEY_LOCATION)));
                p.setLikes(c.getInt(c.getColumnIndex(PostDb.KEY_LIKES)));
                p.setDislikes(c.getInt(c.getColumnIndex(PostDb.KEY_DISLIKES)));
                posts.add(p);
            }while (c.moveToNext());
        }

        adapter = new postListAdapter(getApplicationContext(),posts);

        lvPosts.setAdapter(adapter);

        lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = (Post) parent.getItemAtPosition(position);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                post.getPhoto().compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByte[] = stream.toByteArray();

                Intent i = new Intent(PostsActivity.this,ReadPostActivity.class);
                i.putExtra("id",post.getId());
                i.putExtra("photoinbyte",imageInByte);
                i.putExtra("title",post.getTitle());
                i.putExtra("desc",post.getDescription());
                i.putExtra("autor" ,post.getAuthor());
                i.putExtra("datum",post.getDate());
                i.putExtra("location",post.getLocation());
                i.putExtra("likes",post.getLikes());
                i.putExtra("dislikes",post.getDislikes());
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
            case R.id.add:
                Intent i = new Intent(PostsActivity.this,CreatePostActivity.class);
                startActivity(i);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String queryUri = MyContentProvider.CONTENT_URI2.toString();
        return new CursorLoader(this, Uri.parse(queryUri), null ,null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        dataAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menut, menu);
        return true;
    }


}
