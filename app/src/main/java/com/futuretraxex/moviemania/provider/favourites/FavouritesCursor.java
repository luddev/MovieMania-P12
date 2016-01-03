package com.futuretraxex.moviemania.provider.favourites;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.futuretraxex.moviemania.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code favourites} table.
 */
public class FavouritesCursor extends AbstractCursor implements FavouritesModel {
    public FavouritesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(FavouritesColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Unique TMDB Movie id
     */
    public long getMovieId() {
        Long res = getLongOrNull(FavouritesColumns.MOVIE_ID);
        if (res == null)
            throw new NullPointerException("The value of 'movie_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code original_title} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getOriginalTitle() {
        String res = getStringOrNull(FavouritesColumns.ORIGINAL_TITLE);
        if (res == null)
            throw new NullPointerException("The value of 'original_title' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code overview} value.
     * Cannot be {@code null}.
     */
    @NonNull
    public String getOverview() {
        String res = getStringOrNull(FavouritesColumns.OVERVIEW);
        if (res == null)
            throw new NullPointerException("The value of 'overview' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPosterPath() {
        String res = getStringOrNull(FavouritesColumns.POSTER_PATH);
        return res;
    }

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getReleaseDate() {
        String res = getStringOrNull(FavouritesColumns.RELEASE_DATE);
        return res;
    }

    /**
     * Get the {@code tagline} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getTagline() {
        String res = getStringOrNull(FavouritesColumns.TAGLINE);
        return res;
    }

    /**
     * Get the {@code popularity} value.
     * Can be {@code null}.
     */
    @Nullable
    public Float getPopularity() {
        Float res = getFloatOrNull(FavouritesColumns.POPULARITY);
        return res;
    }

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    public Float getVoteAverage() {
        Float res = getFloatOrNull(FavouritesColumns.VOTE_AVERAGE);
        return res;
    }

    /**
     * Boolean wether movie is adult rated or not
     * Can be {@code null}.
     */
    @Nullable
    public Boolean getAdult() {
        Boolean res = getBooleanOrNull(FavouritesColumns.ADULT);
        return res;
    }

    /**
     * Get the {@code serialized_reviews} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getSerializedReviews() {
        String res = getStringOrNull(FavouritesColumns.SERIALIZED_REVIEWS);
        return res;
    }

    /**
     * Get the {@code serialized_trailers} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getSerializedTrailers() {
        String res = getStringOrNull(FavouritesColumns.SERIALIZED_TRAILERS);
        return res;
    }

    /**
     * Get the {@code is_favourite} value.
     */
    public boolean getIsFavourite() {
        Boolean res = getBooleanOrNull(FavouritesColumns.IS_FAVOURITE);
        if (res == null)
            throw new NullPointerException("The value of 'is_favourite' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
