package com.example.mj.projekat.Database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PostDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_DISLIKES = "dislikes";



    private static final String LOG_TAG = "PostDb";
    public static final String SQLITE_TABLE = "Post";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_TITLE + "," +
                    KEY_DESCRIPTION + "," +
                    KEY_PHOTO + " BLOB," +
                    KEY_AUTHOR + "," +
                    KEY_DATE + "," +
                    KEY_LOCATION + " ," +
                    KEY_LIKES + " integer," +
                    KEY_DISLIKES + " integer," +
                    " UNIQUE (" + KEY_TITLE +"));";

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
