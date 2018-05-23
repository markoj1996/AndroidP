package com.example.mj.projekat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.example.mj.projekat.Database.UsersDb;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE=1;

    Button btn2;
    EditText txtname, txtuname, txtpass;
    ImageView postImage;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        txtname = (EditText) findViewById(R.id.titleTe);
        txtuname = (EditText) findViewById(R.id.DescTe);
        postImage = (ImageView)findViewById(R.id.postImage);

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
                getContentResolver().insert(MyContentProvider.CONTENT_URI2, values);

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
