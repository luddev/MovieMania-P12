package com.futuretraxex.moviemania.provider.favourites;

import android.net.Uri;
import android.provider.BaseColumns;

import com.futuretraxex.moviemania.provider.MovieManiaProvider;
import com.futuretraxex.moviemania.provider.favourites.FavouritesColumns;

/**
 * List of favourites movie.
 */
public class FavouritesColumns implements BaseColumns {
    public static final String TABLE_NAME = "favourites";
    public static final Uri CONTENT_URI = Uri.parse(MovieManiaProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * Unique TMDB Movie id
     */
    public static final String MOVIE_ID = "movie_id";

    public static final String ORIGINAL_TITLE = "original_title";

    public static final String OVERVIEW = "overview";

    public static final String POSTER_PATH = "poster_path";

    public static final String RELEASE_DATE = "release_date";

    public static final String TAGLINE = "tagline";

    public static final String POPULARITY = "popularity";

    public static final String VOTE_AVERAGE = "vote_average";

    /**
     * Boolean wether movie is adult rated or not
     */
    public static final String ADULT = "adult";

    public static final String SERIALIZED_REVIEWS = "serialized_reviews";

    public static final String SERIALIZED_TRAILERS = "serialized_trailers";

    public static final String IS_FAVOURITE = "is_favourite";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MOVIE_ID,
            ORIGINAL_TITLE,
            OVERVIEW,
            POSTER_PATH,
            RELEASE_DATE,
            TAGLINE,
            POPULARITY,
            VOTE_AVERAGE,
            ADULT,
            SERIALIZED_REVIEWS,
            SERIALIZED_TRAILERS,
            IS_FAVOURITE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
            if (c.equals(ORIGINAL_TITLE) || c.contains("." + ORIGINAL_TITLE)) return true;
            if (c.equals(OVERVIEW) || c.contains("." + OVERVIEW)) return true;
            if (c.equals(POSTER_PATH) || c.contains("." + POSTER_PATH)) return true;
            if (c.equals(RELEASE_DATE) || c.contains("." + RELEASE_DATE)) return true;
            if (c.equals(TAGLINE) || c.contains("." + TAGLINE)) return true;
            if (c.equals(POPULARITY) || c.contains("." + POPULARITY)) return true;
            if (c.equals(VOTE_AVERAGE) || c.contains("." + VOTE_AVERAGE)) return true;
            if (c.equals(ADULT) || c.contains("." + ADULT)) return true;
            if (c.equals(SERIALIZED_REVIEWS) || c.contains("." + SERIALIZED_REVIEWS)) return true;
            if (c.equals(SERIALIZED_TRAILERS) || c.contains("." + SERIALIZED_TRAILERS)) return true;
            if (c.equals(IS_FAVOURITE) || c.contains("." + IS_FAVOURITE)) return true;
        }
        return false;
    }

}
