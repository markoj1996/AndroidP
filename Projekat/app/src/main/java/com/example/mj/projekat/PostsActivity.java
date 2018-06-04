package com.example.mj.projekat;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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

import com.example.mj.projekat.Adapter.commentAdapter;
import com.example.mj.projekat.Adapter.postListAdapter;
import com.example.mj.projekat.Database.CommentDb;
import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.PostDb;
import com.example.mj.projekat.Database.TagsDb;
import com.example.mj.projekat.Database.UsersDb;
import com.example.mj.projekat.model.Comment;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.Tag;
import com.example.mj.projekat.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter dataAdapter;


    private DrawerLayout mDrawerLayout;
    private ListView lvPosts;
    private postListAdapter adapter;
    private List<Post> posts;
    ArrayList<Tag> tags = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    String userName;
    String datum;
    String sort;
    Date dateDate=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        final SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
        userName = sharedPref.getString("userName","");

        final SharedPreferences sharedPref2 = getSharedPreferences("datum",MODE_PRIVATE);
        datum = sharedPref2.getString("datum","Datum 1/1/2018");

        String date = datum.split(" ")[1];

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        try {
            dateDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Bundle post = getIntent().getExtras();
        if(post==null)
        {
            return;
        }

        byte imageInByte[] = post.getByteArray("photoinbyte");
        Bitmap image = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);

        lvPosts = (ListView)findViewById(R.id.listView);
        posts = new ArrayList<>();
        getUsers();
        getPosts();
        getTags();

        adapter = new postListAdapter(PostsActivity.this,posts,userName);

        lvPosts.setAdapter(adapter);

        lvPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = (Post) parent.getItemAtPosition(position);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                post.getPhoto().compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByte[] = stream.toByteArray();

                ArrayList<String> tagoviPosta = new ArrayList<>();

                Intent i = new Intent(PostsActivity.this,ReadPostActivity.class);
                for(Tag t : tags)
                {
                    if(t.getPosts()==post.getId())
                    {
                        tagoviPosta.add(t.getName());
                    }
                }
                ByteArrayOutputStream streamU = new ByteArrayOutputStream();
                post.getAuthor().getPhoto().compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByteU[] = streamU.toByteArray();
                String datum;
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String date = df.format(post.getDate());
                i.putExtra("imageUser",imageInByteU);
                i.putExtra("tagovi",tagoviPosta);
                i.putExtra("id",post.getId());
                i.putExtra("photoinbyte",imageInByte);
                i.putExtra("title",post.getTitle());
                i.putExtra("desc",post.getDescription());
                i.putExtra("autor" ,post.getAuthor().getUsername());
                i.putExtra("datum",date);
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
                            case R.id.logout:
                                sharedPref.edit().remove("loggedInUser").commit();
                                Intent il = new Intent(PostsActivity.this,LoginActivity.class);
                                startActivity(il);
                                onBackPressed();
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
                i.putExtra("mode","add");
                byte imageInByte[]=null;
                for(User u :users)
                {
                    if(u.getUsername().equals(userName))
                    {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        u.getPhoto().compress(Bitmap.CompressFormat.JPEG,100,stream);
                        imageInByte = stream.toByteArray();
                    }
                }
                i.putExtra("photoinbyte",imageInByte);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Cursor getPosts()
    {
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
                //p.setAuthor(c.getString(c.getColumnIndex(PostDb.KEY_AUTHOR)));
                //p.setDate(c.getString(c.getColumnIndex(PostDb.KEY_DATE)));
                String autorStr = c.getString(c.getColumnIndex(PostDb.KEY_AUTHOR));
                for(User u : users)
                {
                    if(u.getUsername().equals(autorStr))
                    {
                        p.setAuthor(u);
                        break;
                    }
                }
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                String dateInString = c.getString(c.getColumnIndex(PostDb.KEY_DATE));
                Date date=null;
                try {
                    date = formatter.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                p.setDate(date);
                p.setLocation(c.getString(c.getColumnIndex(PostDb.KEY_LOCATION)));
                p.setLikes(c.getInt(c.getColumnIndex(PostDb.KEY_LIKES)));
                p.setDislikes(c.getInt(c.getColumnIndex(PostDb.KEY_DISLIKES)));
                if(dateDate.before(date))
                {
                    posts.add(p);
                }
            }while (c.moveToNext());
        }
        return c;
    }

    public Cursor getUsers()
    {
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
        return cu;
    }

    public Cursor getTags()
    {
        Cursor ct = getContentResolver().query(MyContentProvider.CONTENT_URI3,null,null,null,null);
        if(ct.moveToFirst())
        {
            do{
                Tag t = new Tag();
                t.setId(ct.getInt(ct.getColumnIndex(TagsDb.KEY_ROWID)));
                t.setName(ct.getString(ct.getColumnIndex(TagsDb.KEY_NAME)));
                t.setPosts(ct.getInt(ct.getColumnIndex(TagsDb.KEY_POST)));
                tags.add(t);
            }while (ct.moveToNext());
        }
        return ct;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        tags.clear();
        getTags();

        SharedPreferences sharedPref2 = getSharedPreferences("sort",MODE_PRIVATE);
        sort = sharedPref2.getString("sort","");

        SharedPreferences sharedPref3 = getSharedPreferences("datum",MODE_PRIVATE);
        datum = sharedPref3.getString("datum","Datum: 1/1/2018");

        String date = datum.split(" ")[1];

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        try {
            dateDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(sort.equals("popularnost"))
        {
            posts.clear();
            getPosts();
            Collections.sort(posts);
            adapter = new postListAdapter(this,posts,userName);
            lvPosts.setAdapter(adapter);
        }else {
            posts.clear();
            getPosts();
            adapter = new postListAdapter(this,posts,userName);
            lvPosts.setAdapter(adapter);
        }

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
