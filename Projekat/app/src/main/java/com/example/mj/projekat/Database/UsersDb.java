package com.example.mj.projekat.Database;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

public class UsersDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    private static final String LOG_TAG = "UsersDb";
    public static final String SQLITE_TABLE = "Users";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_NAME + "," +
                    KEY_PHOTO + "," +
                    KEY_USERNAME + "," +
                    KEY_PASSWORD + "," +
                    " UNIQUE (" + KEY_USERNAME +"));";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }

}
