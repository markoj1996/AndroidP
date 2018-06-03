package com.example.mj.projekat.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.mj.projekat.model.Post;

public class MyContentProvider extends ContentProvider {

    private MyDatabaseHelper dbHelper;

    private static final int ALL_USERS = 1;
    private static final int SINGLE_USER = 2;

    private static final String AUTHORITY = "com.example.mj.projekat.Database";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/users");
    public static final Uri CONTENT_URI2 =
            Uri.parse("content://" + AUTHORITY + "/posts");
    public static final Uri CONTENT_URI3 =
            Uri.parse("content://" + AUTHORITY + "/tags");
    public static final Uri CONTENT_URI4 =
            Uri.parse("content://" + AUTHORITY + "/comments");

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "users", ALL_USERS);
        uriMatcher.addURI(AUTHORITY, "users/#", SINGLE_USER);
        uriMatcher.addURI(AUTHORITY, "posts", 10);
        uriMatcher.addURI(AUTHORITY, "posts/#", 11);
        uriMatcher.addURI(AUTHORITY, "posts/#/c", 12);
        uriMatcher.addURI(AUTHORITY, "posts/#/t", 13);
        uriMatcher.addURI(AUTHORITY, "tags", 20);
        uriMatcher.addURI(AUTHORITY, "tags/#", 21);
        uriMatcher.addURI(AUTHORITY, "comments", 30);
        uriMatcher.addURI(AUTHORITY, "comments/#", 31);

    }

    @Override
    public boolean onCreate() {
        // get access to the database helper
        dbHelper = new MyDatabaseHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                return "vnd.android.cursor.dir/vnd.com.as400samplecode.contentprovider.users";
            case SINGLE_USER:
                return "vnd.android.cursor.item/vnd.com.as400samplecode.contentprovider.users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                long id = db.insert(UsersDb.SQLITE_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CONTENT_URI + "/" + id);
            case 10:
                long id2 = db.insert(PostDb.SQLITE_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CONTENT_URI2 + "/" + id2);
            case 20:
                long id3 = db.insert(TagsDb.SQLITE_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CONTENT_URI3 + "/" + id3);
            case 30:
                long id4 = db.insert(CommentDb.SQLITE_TABLE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.parse(CONTENT_URI4 + "/" + id4);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                queryBuilder.setTables(UsersDb.SQLITE_TABLE);
                cursor = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case SINGLE_USER:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(UsersDb.KEY_ROWID + "=" + id);
                break;
            case 10:
                queryBuilder.setTables(PostDb.SQLITE_TABLE);
                cursor = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case 11:
                break;
            case 20:
                queryBuilder.setTables(TagsDb.SQLITE_TABLE);
                cursor = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case 30:
                queryBuilder.setTables(CommentDb.SQLITE_TABLE);
                cursor = queryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return cursor;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deleteCount = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                deleteCount = db.delete(UsersDb.SQLITE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SINGLE_USER:
                String id = uri.getPathSegments().get(1);
                selection = UsersDb.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            case 10:
                deleteCount = db.delete(PostDb.SQLITE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
            case 11:
                String id1 = uri.getPathSegments().get(1);
                selection = PostDb.KEY_ROWID + "=" + id1;
                deleteCount = db.delete(PostDb.SQLITE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case 12:
                String id14 = uri.getPathSegments().get(1);
                selection = CommentDb.KEY_POST + "=" + id14;
                deleteCount = db.delete(CommentDb.SQLITE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case 13:
                String id15 = uri.getPathSegments().get(1);
                selection = TagsDb.KEY_POST + "=" + id15;
                deleteCount = db.delete(TagsDb.SQLITE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case 21:
                String id2 = uri.getPathSegments().get(1);
                selection = TagsDb.KEY_ROWID + "=" + id2;
                deleteCount = db.delete(TagsDb.SQLITE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case 31:
                String id3 = uri.getPathSegments().get(1);
                selection = CommentDb.KEY_ROWID + "=" + id3;
                deleteCount = db.delete(CommentDb.SQLITE_TABLE, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateCount = 0;
        switch (uriMatcher.match(uri)) {
            case ALL_USERS:
                updateCount = db.update(UsersDb.SQLITE_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return updateCount;
            case SINGLE_USER:
                String id = uri.getPathSegments().get(1);
                selection = UsersDb.KEY_ROWID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            case 10:
                updateCount = db.update(PostDb.SQLITE_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return updateCount;
            case 11:
                String id2 = uri.getPathSegments().get(1);
                selection = PostDb.KEY_ROWID + "=" + id2;
                updateCount = db.update(PostDb.SQLITE_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case 30:
                updateCount = db.update(CommentDb.SQLITE_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return updateCount;
            case 31:
                String id3 = uri.getPathSegments().get(1);
                selection = CommentDb.KEY_ROWID + "=" + id3;
                updateCount = db.update(CommentDb.SQLITE_TABLE, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return updateCount;
    }

}