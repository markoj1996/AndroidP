package com.example.mj.projekat;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.MyDatabaseHelper;
import com.example.mj.projekat.Database.UsersDb;
import com.example.mj.projekat.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter dataAdapter;

    MyDatabaseHelper mdh = new MyDatabaseHelper(this);

    Button _btnLogin;
    EditText _txtUName, _txtPass;
    private  String id;
    ArrayList<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _txtUName=(EditText)findViewById(R.id.username);
        _txtPass=(EditText)findViewById(R.id.password);
        _btnLogin=(Button)findViewById(R.id.button);

        getAllUsers();

        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = getContentResolver().query(MyContentProvider.CONTENT_URI,null,null,null,null);

                final ArrayList<User>users = new ArrayList<>();

                if(c.moveToFirst())
                {
                    do{
                        User u = new User();
                        u.setId(c.getColumnIndex(UsersDb.KEY_ROWID));
                        u.setName(c.getString(c.getColumnIndex(UsersDb.KEY_NAME)));
                        u.setUsername(c.getString(c.getColumnIndex(UsersDb.KEY_USERNAME)));
                        u.setPassword(c.getString(c.getColumnIndex(UsersDb.KEY_PASSWORD)));
                        byte imageInByte[] = c.getBlob(2);
                        Bitmap image = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);
                        u.setPhoto(image);
                        users.add(u);
                    }while (c.moveToNext());
                }

                String uname = _txtUName.getText().toString();
                String pass = _txtPass.getText().toString();
                String m = "Uspesna prijava";

                for(User u : users)
                {
                    if(u.getUsername().equals(uname)&&u.getPassword().equals(pass))
                    {
                        SharedPreferences sharedPref = getSharedPreferences("loggedInUser",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("userName",uname);
                        editor.apply();
                        Toast.makeText(LoginActivity.this,m,Toast.LENGTH_SHORT).show();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        u.getPhoto().compress(Bitmap.CompressFormat.JPEG,100,stream);
                        byte imageInByte[] = stream.toByteArray();

                        Intent i = new Intent(LoginActivity.this,PostsActivity.class);
                        i.putExtra("photoinbyte",imageInByte);
                        startActivity(i);
                        onBackPressed();
                        break;
                    }
                    m="Pogresan username ili password";
                    Toast.makeText(LoginActivity.this,m,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getAllUsers() {


        // The desired columns to be bound
        String[] columns = new String[] {
                UsersDb.KEY_NAME,
                UsersDb.KEY_USERNAME,
                UsersDb.KEY_PASSWORD
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.name,
                R.id.username,
                R.id.password,
        };

        // create an adapter from the SimpleCursorAdapter
        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.country_info,
                null,
                columns,
                to,
                0);

        // get reference to the ListView
        //ListView listView = (ListView) findViewById(R.id.countryList);
        // Assign adapter to ListView
        //listView.setAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);

    }

    public void btnSignUpActivity(View view) {
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
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
        getLoaderManager().restartLoader(0, null, this);
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

        String queryUri = MyContentProvider.CONTENT_URI.toString();
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
        //getMenuInflater().inflate(R.Menu.activity_main, menu);
        return true;
    }
}
