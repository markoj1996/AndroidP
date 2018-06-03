package com.example.mj.projekat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mj.projekat.Database.MyContentProvider;
import com.example.mj.projekat.Database.UsersDb;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE=1;

    Button btn2;
    EditText txtname, txtuname, txtpass;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtname = (EditText) findViewById(R.id.name);
        txtuname = (EditText) findViewById(R.id.uname);
        txtpass = (EditText)findViewById(R.id.pass);
        image = (ImageView)findViewById(R.id.addImage);

        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        image.setOnClickListener(this);

    }

    public void onClick(View v) {

        // get values from the spinner and the input text fields
        String myname = txtname.getText().toString();
        String myuname = txtuname.getText().toString();
        String mypass = txtpass.getText().toString();

        switch (v.getId()) {
            case R.id.button2:
                // check for blanks
                if(myname.trim().equalsIgnoreCase("")){
                    Toast.makeText(getBaseContext(), "Please ENTER name", Toast.LENGTH_LONG).show();
                    return;
                }

                // check for blanks
                if(myuname.trim().equalsIgnoreCase("")){
                    Toast.makeText(getBaseContext(), "Please ENTER user name", Toast.LENGTH_LONG).show();
                    return;
                }

                // check for blanks
                if(mypass.trim().equalsIgnoreCase("")){
                    Toast.makeText(getBaseContext(), "Please ENTER password", Toast.LENGTH_LONG).show();
                    return;
                }

                Bitmap userImage = null;
                try {
                    userImage = ((BitmapDrawable)image.getDrawable()).getBitmap();
                }catch (RuntimeException c)
                {
                    Toast.makeText(getBaseContext(), "Please ENTER photo", Toast.LENGTH_LONG).show();
                    return;
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                userImage.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte imageInByte[] = stream.toByteArray();

                ContentValues values = new ContentValues();
                values.put(UsersDb.KEY_NAME, myname);
                values.put(UsersDb.KEY_USERNAME, myuname);
                values.put(UsersDb.KEY_PASSWORD, mypass);
                values.put(UsersDb.KEY_PHOTO,imageInByte);
                getContentResolver().insert(MyContentProvider.CONTENT_URI, values);

                finish();
                break;
            case R.id.addImage:
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
            image.setImageURI(selectedImage);
        }
    }
}
