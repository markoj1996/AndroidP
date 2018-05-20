package com.example.mj.projekat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadPostActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_post);

        Bundle post = getIntent().getExtras();
        if(post==null)
        {
            return;
        }
        String naslov = post.getString("title");
        final TextView title = (TextView)findViewById(R.id.titleTV);
        title.setText(naslov);
        int like = (int) post.get("like");
        int dislike = (int) post.get("dislike");
        final TextView info2 = (TextView)findViewById(R.id.info2);
        info2.setText("Like: "+like + "   " +"Dislike: "+dislike);
        String desc = post.getString("desc");
        final TextView descr = (TextView)findViewById(R.id.sadrzaj);
        descr.setText(desc);
        Bitmap b = (Bitmap) post.get("photo");
        final ImageView img = (ImageView)findViewById(R.id.slika);
        img.setImageBitmap(b);


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
                                Intent i = new Intent(ReadPostActivity.this, PostsActivity.class);
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
