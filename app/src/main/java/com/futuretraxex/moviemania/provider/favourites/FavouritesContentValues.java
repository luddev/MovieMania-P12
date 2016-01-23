package com.futuretraxex.moviemania.provider.favourites;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.futuretraxex.moviemania.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code favourites} table.
 */
public class FavouritesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return FavouritesColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable FavouritesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable FavouritesSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Unique TMDB Movie id
     */
    public FavouritesContentValues putMovieId(long value) {
        mContentValues.put(FavouritesColumns.MOVIE_ID, value);
        return this;
    }


    public FavouritesContentValues putOriginalTitle(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("originalTitle must not be null");
        mContentValues.put(FavouritesColumns.ORIGINAL_TITLE, value);
        return this;
    }


    public FavouritesContentValues putOverview(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("overview must not be null");
        mContentValues.put(FavouritesColumns.OVERVIEW, value);
        return this;
    }


    public FavouritesContentValues putBackdropPath(@Nullable String value) {
        mContentValues.put(FavouritesColumns.BACKDROP_PATH, value);
        return this;
    }

    public FavouritesContentValues putBackdropPathNull() {
        mContentValues.putNull(FavouritesColumns.BACKDROP_PATH);
        return this;
    }

    public FavouritesContentValues putPosterPath(@Nullable String value) {
        mContentValues.put(FavouritesColumns.POSTER_PATH, value);
        return this;
    }

    public FavouritesContentValues putPosterPathNull() {
        mContentValues.putNull(FavouritesColumns.POSTER_PATH);
        return this;
    }

    public FavouritesContentValues putReleaseDate(@Nullable String value) {
        mContentValues.put(FavouritesColumns.RELEASE_DATE, value);
        return this;
    }

    public FavouritesContentValues putReleaseDateNull() {
        mContentValues.putNull(FavouritesColumns.RELEASE_DATE);
        return this;
    }

    public FavouritesContentValues putTagline(@Nullable String value) {
        mContentValues.put(FavouritesColumns.TAGLINE, value);
        return this;
    }

    public FavouritesContentValues putTaglineNull() {
        mContentValues.putNull(FavouritesColumns.TAGLINE);
        return this;
    }

    public FavouritesContentValues putPopularity(@Nullable Float value) {
        mContentValues.put(FavouritesColumns.POPULARITY, value);
        return this;
    }

    public FavouritesContentValues putPopularityNull() {
        mContentValues.putNull(FavouritesColumns.POPULARITY);
        return this;
    }

    public FavouritesContentValues putVoteAverage(@Nullable Float value) {
        mContentValues.put(FavouritesColumns.VOTE_AVERAGE, value);
        return this;
    }

    public FavouritesContentValues putVoteAverageNull() {
        mContentValues.putNull(FavouritesColumns.VOTE_AVERAGE);
        return this;
    }

    /**
     * Boolean wether movie is adult rated or not
     */
    public FavouritesContentValues putAdult(@Nullable Boolean value) {
        mContentValues.put(FavouritesColumns.ADULT, value);
        return this;
    }

    public FavouritesContentValues putAdultNull() {
        mContentValues.putNull(FavouritesColumns.ADULT);
        return this;
    }

    public FavouritesContentValues putSerializedReviews(@Nullable String value) {
        mContentValues.put(FavouritesColumns.SERIALIZED_REVIEWS, value);
        return this;
    }

    public FavouritesContentValues putSerializedReviewsNull() {
        mContentValues.putNull(FavouritesColumns.SERIALIZED_REVIEWS);
        return this;
    }

    public FavouritesContentValues putSerializedTrailers(@Nullable String value) {
        mContentValues.put(FavouritesColumns.SERIALIZED_TRAILERS, value);
        return this;
    }

    public FavouritesContentValues putSerializedTrailersNull() {
        mContentValues.putNull(FavouritesColumns.SERIALIZED_TRAILERS);
        return this;
    }

    public FavouritesContentValues putIsFavourite(boolean value) {
        mContentValues.put(FavouritesColumns.IS_FAVOURITE, value);
        return this;
    }

}
