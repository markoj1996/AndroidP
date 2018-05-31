package com.example.mj.projekat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.PostDb;
import com.example.mj.projekat.Database.TagsDb;
import com.example.mj.projekat.Database.UsersDb;
import com.example.mj.projekat.model.Post;
import com.example.mj.projekat.model.Tag;
import com.example.mj.projekat.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE=1;

    Button btn2;
    EditText txtname, txtuname, txtpass;
    ImageView postImage;
    ArrayList<Post> posts= new ArrayList<>();
    ArrayList<Tag> tags= new ArrayList<>();
    List<User> users = new ArrayList<>();
    String mode;
    Post p;
    int postId;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        if (this.getIntent().getExtras() != null){
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
            postId = bundle.getInt("post");
        }

        txtname = (EditText) findViewById(R.id.titleTe);
        txtuname = (EditText) findViewById(R.id.DescTe);
        txtpass = (EditText) findViewById(R.id.TagTe);
        postImage = (ImageView)findViewById(R.id.postImage);

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
                String autorStr = c.getString(c.getColumnIndex(PostDb.KEY_AUTHOR));
                for(User u : users)
                {
                    if(u.getUsername().equals(autorStr))
                    {
                        p.setAuthor(u);
                    }
                }
                p.setDate(c.getString(c.getColumnIndex(PostDb.KEY_DATE)));
                p.setLocation(c.getString(c.getColumnIndex(PostDb.KEY_LOCATION)));
                p.setLikes(c.getInt(c.getColumnIndex(PostDb.KEY_LIKES)));
                p.setDislikes(c.getInt(c.getColumnIndex(PostDb.KEY_DISLIKES)));
                posts.add(p);
            }while (c.moveToNext());
        }

        for(Post po : posts)
        {
            if(po.getId()==postId)
            {
                p = po;
                break;
            }
        }

        /*Cursor ct = getContentResolver().query(MyContentProvider.CONTENT_URI3,null,null,null,null);

        if(cu.moveToFirst())
        {
            do{
                Tag t = new Tag();
                t.setId(ct.getInt(ct.getColumnIndex(TagsDb.KEY_ROWID)));
                t.setName(ct.getString(ct.getColumnIndex(TagsDb.KEY_NAME)));
                t.setPosts(ct.getInt(ct.getColumnIndex(TagsDb.KEY_POST)));
                tags.add(t);
            }while (cu.moveToNext());
        }*/

        if(mode.equals("update"))
        {
            txtname.setText(p.getTitle());
            txtuname.setText(p.getDescription());
            postImage.setImageBitmap(p.getPhoto());

        }

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
        SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
        String author = sharedPref.getString("userName","");
        Date datum = new Date();
        String location="Location";

        switch (v.getId()) {
            case R.id.createButton:
                Bitmap image = ((BitmapDrawable)postImage.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByte[] = stream.toByteArray();

                ContentValues values = new ContentValues();
                values.put(PostDb.KEY_TITLE, myTitle);
                values.put(PostDb.KEY_DESCRIPTION, myDesc);
                values.put(PostDb.KEY_PHOTO,imageInByte);
                values.put(PostDb.KEY_AUTHOR,author);
                values.put(PostDb.KEY_DATE,datum.toString());
                values.put(PostDb.KEY_LOCATION,location);
                values.put(PostDb.KEY_LIKES,0);
                values.put(PostDb.KEY_DISLIKES,0);

                if(mode.trim().equalsIgnoreCase("add")){
                    values.put(PostDb.KEY_ROWID,posts.size()+1);
                    getContentResolver().insert(MyContentProvider.CONTENT_URI2, values);
                }else {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI2 + "/" + postId);
                    getContentResolver().update(uri, values, null, null);
                }

                /*String tagovi = txtpass.getText().toString();
                String[] niz = tagovi.split(" ");
                List<String> lista = Arrays.asList(niz);
                int id = tags.size();
                for(String tag : lista)
                {
                    Tag t = new Tag();
                    t.setId(id+1);
                    id++;
                    t.setName(tag);
                    t.setPosts(posts.size()+1);
                    ContentValues values2 = new ContentValues();
                    values2.put(TagsDb.KEY_ROWID,t.getId());
                    values2.put(TagsDb.KEY_NAME,t.getName());
                    values2.put(TagsDb.KEY_POST,t.getPosts());
                    if(mode.trim().equalsIgnoreCase("add")) {
                        getContentResolver().insert(MyContentProvider.CONTENT_URI3, values2);
                    }
                }*/

                finish();
                break;
            case R.id.postImage:
                Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galeryIntent,RESULT_LOAD_IMAGE);
                break;
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
