package com.example.mj.projekat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.PostDb;
import com.example.mj.projekat.Database.TagsDb;
import com.example.mj.projekat.Database.UsersDb;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.Tag;
import com.example.mj.projekat.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;

    Button btn2;
    Button cancel;
    EditText txtname, txtuname, txtpass;
    ImageView postImage;
    ArrayList<Post> posts = new ArrayList<>();
    ArrayList<Tag> tags = new ArrayList<>();
    List<User> users = new ArrayList<>();
    String mode;
    Post p;
    int postId;
    String userName;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        final SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
        userName = sharedPref.getString("userName","");

        Bundle post = getIntent().getExtras();
        if(post==null)
        {
            return;
        }

        //byte imageInByte[] = post.getByteArray("photoinbyte");
        //Bitmap image = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);

        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
            postId = bundle.getInt("post");
        }

        txtname = (EditText) findViewById(R.id.titleTe);
        txtuname = (EditText) findViewById(R.id.DescTe);
        txtpass = (EditText) findViewById(R.id.TagTe);
        postImage = (ImageView) findViewById(R.id.postImage);

        Cursor cu = getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, null);

        if (cu.moveToFirst()) {
            do {
                User u = new User();
                u.setId(cu.getColumnIndex(UsersDb.KEY_ROWID));
                u.setName(cu.getString(cu.getColumnIndex(UsersDb.KEY_NAME)));
                u.setUsername(cu.getString(cu.getColumnIndex(UsersDb.KEY_USERNAME)));
                u.setPassword(cu.getString(cu.getColumnIndex(UsersDb.KEY_PASSWORD)));
                byte PhotoInByte[] = cu.getBlob(2);
                Bitmap sl = BitmapFactory.decodeByteArray(PhotoInByte, 0, PhotoInByte.length);
                u.setPhoto(sl);
                users.add(u);
            } while (cu.moveToNext());
        }

        Cursor c = getContentResolver().query(MyContentProvider.CONTENT_URI2, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                Post p = new Post();
                p.setId(c.getInt(c.getColumnIndex(PostDb.KEY_ROWID)));
                p.setTitle(c.getString(c.getColumnIndex(PostDb.KEY_TITLE)));
                p.setDescription(c.getString(c.getColumnIndex(PostDb.KEY_DESCRIPTION)));
                byte PostImageInByte[] = c.getBlob(3);
                Bitmap Pimage = BitmapFactory.decodeByteArray(PostImageInByte, 0, PostImageInByte.length);
                p.setPhoto(Pimage);
                String autorStr = c.getString(c.getColumnIndex(PostDb.KEY_AUTHOR));
                for (User u : users) {
                    if (u.getUsername().equals(autorStr)) {
                        p.setAuthor(u);
                        break;
                    }
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
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
                posts.add(p);
            } while (c.moveToNext());
        }

        for (Post po : posts) {
            if (po.getId() == postId) {
                p = po;
                break;
            }
        }

        Cursor ct = getContentResolver().query(MyContentProvider.CONTENT_URI3,null,null,null,null);

        if(ct.moveToFirst())
        {
            do{
                Tag t = new Tag();
                t.setId(ct.getInt(ct.getColumnIndex(TagsDb.KEY_ROWID)));
                t.setName(ct.getString(ct.getColumnIndex(TagsDb.KEY_NAME)));
                t.setPosts(ct.getInt(ct.getColumnIndex(TagsDb.KEY_POST)));
                tags.add(t);
                ArrayList<Tag> tagovi=new ArrayList<>();
                if(t.getPosts()==postId)
                {
                    tagovi.add(t);
                    p.setTags(tagovi);
                }
            }while (ct.moveToNext());
        }

        if (mode.equals("update")) {
            txtname.setText(p.getTitle());
            txtuname.setText(p.getDescription());
            postImage.setImageBitmap(p.getPhoto());
            String tags = "";
            for(Tag t : p.getTags())
            {
                tags = tags + " "+t.getName();
            }
            txtpass.setText(tags);

        }

        cancel = (Button)findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(this);

        btn2 = (Button) findViewById(R.id.createButton);
        btn2.setOnClickListener(this);
        postImage.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create post");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.loggedInUser);
        nav_user.setText(userName);
        ImageView profile = (ImageView)hView.findViewById(R.id.avatar);
        //profile.setImageBitmap(image);

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
                                Intent i = new Intent(CreatePostActivity.this, PostsActivity.class);
                                startActivity(i);
                                return true;
                            case R.id.settings:
                                Intent ii = new Intent(CreatePostActivity.this, SettingsActivity.class);
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

    public void onClick(View v) {

        // get values from the spinner and the input text fields
        String myTitle = txtname.getText().toString();
        String myDesc = txtuname.getText().toString();
        String tagovi = txtpass.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
        String author = sharedPref.getString("userName","");

        String pattern = "MM/dd/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        String location="Location";

        switch (v.getId()) {
            case R.id.createButton:

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



                // check for blanks
                if(tagovi.trim().equalsIgnoreCase("")){
                    Toast.makeText(getBaseContext(), "Please ENTER tags", Toast.LENGTH_LONG).show();
                    return;
                }
                Bitmap image = null;
                try {
                    image = ((BitmapDrawable)postImage.getDrawable()).getBitmap();
                }catch (RuntimeException c)
                {
                    Toast.makeText(getBaseContext(), "Please ENTER photo", Toast.LENGTH_LONG).show();
                    return;
                }



                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByte[] = stream.toByteArray();

                if(mode.trim().equalsIgnoreCase("add")){
                    ContentValues values = new ContentValues();
                    values.put(PostDb.KEY_TITLE, myTitle);
                    values.put(PostDb.KEY_DESCRIPTION, myDesc);
                    values.put(PostDb.KEY_PHOTO,imageInByte);
                    values.put(PostDb.KEY_AUTHOR,author);
                    values.put(PostDb.KEY_DATE,date);
                    values.put(PostDb.KEY_LOCATION,location);
                    values.put(PostDb.KEY_LIKES,0);
                    values.put(PostDb.KEY_DISLIKES,0);
                    values.put(PostDb.KEY_ROWID,posts.size()+1);
                    getContentResolver().insert(MyContentProvider.CONTENT_URI2, values);
                }else {
                    ContentValues values = new ContentValues();
                    values.put(PostDb.KEY_TITLE, myTitle);
                    values.put(PostDb.KEY_DESCRIPTION, myDesc);
                    values.put(PostDb.KEY_PHOTO,imageInByte);
                    values.put(PostDb.KEY_AUTHOR,author);
                    values.put(PostDb.KEY_DATE,date);
                    values.put(PostDb.KEY_LOCATION,location);
                    for(Post p : posts)
                    {
                        if(p.getId()==postId)
                        {
                            values.put(PostDb.KEY_LIKES,p.getLikes());
                            values.put(PostDb.KEY_DISLIKES,p.getDislikes());
                        }
                    }
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI2 + "/" + postId);
                    getContentResolver().update(uri, values, null, null);
                }

                String[] niz = tagovi.split(" ");
                List<String> lista = Arrays.asList(niz);
                int id = tags.size();
                for(String tag : lista)
                {
                    if(mode.trim().equalsIgnoreCase("add")) {
                        Tag t = new Tag();
                        t.setId(id+1);
                        id++;
                        t.setName(tag);
                        t.setPosts(posts.size()+1);
                        ContentValues values2 = new ContentValues();
                        values2.put(TagsDb.KEY_ROWID,t.getId());
                        values2.put(TagsDb.KEY_NAME,t.getName());
                        values2.put(TagsDb.KEY_POST,t.getPosts());
                        getContentResolver().insert(MyContentProvider.CONTENT_URI3, values2);
                    }else {
                        Tag t = new Tag();
                        t.setId(id+1);
                        id++;
                        t.setName(tag);
                        t.setPosts(postId);
                        ContentValues values2 = new ContentValues();
                        values2.put(TagsDb.KEY_ROWID,t.getId());
                        values2.put(TagsDb.KEY_NAME,t.getName());
                        values2.put(TagsDb.KEY_POST,t.getPosts());
                        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI2 + "/" + postId + "/"+"t");
                        getContentResolver().delete(uri,null,null);
                        getContentResolver().insert(MyContentProvider.CONTENT_URI3, values2);
                    }
                }

                finish();
                break;
            case R.id.postImage:
                Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeryIntent,RESULT_LOAD_IMAGE);
                break;
            case R.id.cancelBtn:
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null)
        {
            Uri selectedImage = data.getData();
            postImage.setImageURI(selectedImage);
        }
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
