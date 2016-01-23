package com.futuretraxex.moviemania.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.futuretraxex.moviemania.BuildConfig;
import com.futuretraxex.moviemania.provider.favourites.FavouritesColumns;

public class MovieManiaDbHelper extends SQLiteOpenHelper {
    private static final String TAG = MovieManiaDbHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "moviemania.db";
    private static final int DATABASE_VERSION = 1;
    private static MovieManiaDbHelper sInstance;
    private final Context mContext;
    private final MovieManiaDbHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_FAVOURITES = "CREATE TABLE IF NOT EXISTS "
            + FavouritesColumns.TABLE_NAME + " ( "
            + FavouritesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FavouritesColumns.MOVIE_ID + " INTEGER NOT NULL, "
            + FavouritesColumns.ORIGINAL_TITLE + " TEXT NOT NULL, "
            + FavouritesColumns.OVERVIEW + " TEXT NOT NULL, "
            + FavouritesColumns.BACKDROP_PATH + " TEXT, "
            + FavouritesColumns.POSTER_PATH + " TEXT, "
            + FavouritesColumns.RELEASE_DATE + " TEXT, "
            + FavouritesColumns.TAGLINE + " TEXT, "
            + FavouritesColumns.POPULARITY + " REAL, "
            + FavouritesColumns.VOTE_AVERAGE + " REAL, "
            + FavouritesColumns.ADULT + " INTEGER, "
            + FavouritesColumns.SERIALIZED_REVIEWS + " TEXT, "
            + FavouritesColumns.SERIALIZED_TRAILERS + " TEXT, "
            + FavouritesColumns.IS_FAVOURITE + " INTEGER NOT NULL "
            + ", CONSTRAINT unique_id UNIQUE (movie_id) ON CONFLICT REPLACE"
            + " );";

    // @formatter:on

    public static MovieManiaDbHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static MovieManiaDbHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static MovieManiaDbHelper newInstancePreHoneycomb(Context context) {
        return new MovieManiaDbHelper(context);
    }

    private MovieManiaDbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new MovieManiaDbHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static MovieManiaDbHelper newInstancePostHoneycomb(Context context) {
        return new MovieManiaDbHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private MovieManiaDbHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new MovieManiaDbHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_FAVOURITES);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
