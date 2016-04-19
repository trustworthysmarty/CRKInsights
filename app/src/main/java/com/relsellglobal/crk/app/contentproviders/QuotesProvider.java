package com.relsellglobal.crk.app.contentproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

/**
 * Created by anilkukreti on 02/03/16.
 */
public class QuotesProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.relsellglobal.crk.app.contentproviders.QuotesProvider";


    public static class QuotesTable {
        public static final String URL = "content://" + PROVIDER_NAME + "/quotestable";
        public static final Uri CONTENT_URI = Uri.parse(URL);
        public static final String _ID = "_id";
        public static final String DESC = "name";
        public static final String CATEGORY = "category";
        public static final String CATEGORY_ID = "category_id";
        public static final String AUTHOR = "author";
        public static final String FAVORITE="favorite";
    }


    private static HashMap<String, String> QUOTES_PROJECTION_MAP;

    static final int QUOTE = 1;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "quotestable", QUOTE);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "QuotesDb";
    public static final String QUOTES_TABLE_NAME = "quotestable";
    static final int DATABASE_VERSION = 4;

    static final String CREATE_QUOTE_DB_TABLE =
            " CREATE TABLE " + QUOTES_TABLE_NAME +
                    " (" + QuotesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QuotesTable.DESC + " TEXT NOT NULL, " +
                    QuotesTable.CATEGORY + " TEXT NOT NULL, " +
                    QuotesTable.AUTHOR + " TEXT NOT NULL, " +
                    QuotesTable.FAVORITE + " TEXT, " +
                    QuotesTable.CATEGORY_ID + "  TEXT NOT NULL);";





    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_QUOTE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + QUOTES_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        String tableName = getTableName(uri);

        long rowID = db.insert(tableName, "", values);
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(QuotesTable.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        switch (uriMatcher.match(uri)) {
            case QUOTE:
                qb.setTables(QUOTES_TABLE_NAME);
                qb.setProjectionMap(QUOTES_PROJECTION_MAP);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI While query" + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on student names
             */
            sortOrder = QuotesTable._ID;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case QUOTE:
                count = db.delete(QUOTES_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case QUOTE:
                count = db.update(QUOTES_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI while updating" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public String getTableName(Uri uri) {
        String result = null;
        int res = uriMatcher.match(uri);
        switch (res) {
            case QUOTE:
                result = QUOTES_TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI Name in table: " + uri);
        }
        return result;
    }


}