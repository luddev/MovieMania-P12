package com.futuretraxex.moviemania.provider.favourites;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.futuretraxex.moviemania.provider.base.AbstractSelection;

/**
 * Selection for the {@code favourites} table.
 */
public class FavouritesSelection extends AbstractSelection<FavouritesSelection> {
    @Override
    protected Uri baseUri() {
        return FavouritesColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code FavouritesCursor} object, which is positioned before the first entry, or null.
     */
    public FavouritesCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new FavouritesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public FavouritesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code FavouritesCursor} object, which is positioned before the first entry, or null.
     */
    public FavouritesCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new FavouritesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public FavouritesCursor query(Context context) {
        return query(context, null);
    }


    public FavouritesSelection id(long... value) {
        addEquals("favourites." + FavouritesColumns._ID, toObjectArray(value));
        return this;
    }

    public FavouritesSelection idNot(long... value) {
        addNotEquals("favourites." + FavouritesColumns._ID, toObjectArray(value));
        return this;
    }

    public FavouritesSelection orderById(boolean desc) {
        orderBy("favourites." + FavouritesColumns._ID, desc);
        return this;
    }

    public FavouritesSelection orderById() {
        return orderById(false);
    }

    public FavouritesSelection movieId(long... value) {
        addEquals(FavouritesColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public FavouritesSelection movieIdNot(long... value) {
        addNotEquals(FavouritesColumns.MOVIE_ID, toObjectArray(value));
        return this;
    }

    public FavouritesSelection movieIdGt(long value) {
        addGreaterThan(FavouritesColumns.MOVIE_ID, value);
        return this;
    }

    public FavouritesSelection movieIdGtEq(long value) {
        addGreaterThanOrEquals(FavouritesColumns.MOVIE_ID, value);
        return this;
    }

    public FavouritesSelection movieIdLt(long value) {
        addLessThan(FavouritesColumns.MOVIE_ID, value);
        return this;
    }

    public FavouritesSelection movieIdLtEq(long value) {
        addLessThanOrEquals(FavouritesColumns.MOVIE_ID, value);
        return this;
    }

    public FavouritesSelection orderByMovieId(boolean desc) {
        orderBy(FavouritesColumns.MOVIE_ID, desc);
        return this;
    }

    public FavouritesSelection orderByMovieId() {
        orderBy(FavouritesColumns.MOVIE_ID, false);
        return this;
    }

    public FavouritesSelection originalTitle(String... value) {
        addEquals(FavouritesColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public FavouritesSelection originalTitleNot(String... value) {
        addNotEquals(FavouritesColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public FavouritesSelection originalTitleLike(String... value) {
        addLike(FavouritesColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public FavouritesSelection originalTitleContains(String... value) {
        addContains(FavouritesColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public FavouritesSelection originalTitleStartsWith(String... value) {
        addStartsWith(FavouritesColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public FavouritesSelection originalTitleEndsWith(String... value) {
        addEndsWith(FavouritesColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public FavouritesSelection orderByOriginalTitle(boolean desc) {
        orderBy(FavouritesColumns.ORIGINAL_TITLE, desc);
        return this;
    }

    public FavouritesSelection orderByOriginalTitle() {
        orderBy(FavouritesColumns.ORIGINAL_TITLE, false);
        return this;
    }

    public FavouritesSelection overview(String... value) {
        addEquals(FavouritesColumns.OVERVIEW, value);
        return this;
    }

    public FavouritesSelection overviewNot(String... value) {
        addNotEquals(FavouritesColumns.OVERVIEW, value);
        return this;
    }

    public FavouritesSelection overviewLike(String... value) {
        addLike(FavouritesColumns.OVERVIEW, value);
        return this;
    }

    public FavouritesSelection overviewContains(String... value) {
        addContains(FavouritesColumns.OVERVIEW, value);
        return this;
    }

    public FavouritesSelection overviewStartsWith(String... value) {
        addStartsWith(FavouritesColumns.OVERVIEW, value);
        return this;
    }

    public FavouritesSelection overviewEndsWith(String... value) {
        addEndsWith(FavouritesColumns.OVERVIEW, value);
        return this;
    }

    public FavouritesSelection orderByOverview(boolean desc) {
        orderBy(FavouritesColumns.OVERVIEW, desc);
        return this;
    }

    public FavouritesSelection orderByOverview() {
        orderBy(FavouritesColumns.OVERVIEW, false);
        return this;
    }

    public FavouritesSelection backdropPath(String... value) {
        addEquals(FavouritesColumns.BACKDROP_PATH, value);
        return this;
    }

    public FavouritesSelection backdropPathNot(String... value) {
        addNotEquals(FavouritesColumns.BACKDROP_PATH, value);
        return this;
    }

    public FavouritesSelection backdropPathLike(String... value) {
        addLike(FavouritesColumns.BACKDROP_PATH, value);
        return this;
    }

    public FavouritesSelection backdropPathContains(String... value) {
        addContains(FavouritesColumns.BACKDROP_PATH, value);
        return this;
    }

    public FavouritesSelection backdropPathStartsWith(String... value) {
        addStartsWith(FavouritesColumns.BACKDROP_PATH, value);
        return this;
    }

    public FavouritesSelection backdropPathEndsWith(String... value) {
        addEndsWith(FavouritesColumns.BACKDROP_PATH, value);
        return this;
    }

    public FavouritesSelection orderByBackdropPath(boolean desc) {
        orderBy(FavouritesColumns.BACKDROP_PATH, desc);
        return this;
    }

    public FavouritesSelection orderByBackdropPath() {
        orderBy(FavouritesColumns.BACKDROP_PATH, false);
        return this;
    }

    public FavouritesSelection posterPath(String... value) {
        addEquals(FavouritesColumns.POSTER_PATH, value);
        return this;
    }

    public FavouritesSelection posterPathNot(String... value) {
        addNotEquals(FavouritesColumns.POSTER_PATH, value);
        return this;
    }

    public FavouritesSelection posterPathLike(String... value) {
        addLike(FavouritesColumns.POSTER_PATH, value);
        return this;
    }

    public FavouritesSelection posterPathContains(String... value) {
        addContains(FavouritesColumns.POSTER_PATH, value);
        return this;
    }

    public FavouritesSelection posterPathStartsWith(String... value) {
        addStartsWith(FavouritesColumns.POSTER_PATH, value);
        return this;
    }

    public FavouritesSelection posterPathEndsWith(String... value) {
        addEndsWith(FavouritesColumns.POSTER_PATH, value);
        return this;
    }

    public FavouritesSelection orderByPosterPath(boolean desc) {
        orderBy(FavouritesColumns.POSTER_PATH, desc);
        return this;
    }

    public FavouritesSelection orderByPosterPath() {
        orderBy(FavouritesColumns.POSTER_PATH, false);
        return this;
    }

    public FavouritesSelection releaseDate(String... value) {
        addEquals(FavouritesColumns.RELEASE_DATE, value);
        return this;
    }

    public FavouritesSelection releaseDateNot(String... value) {
        addNotEquals(FavouritesColumns.RELEASE_DATE, value);
        return this;
    }

    public FavouritesSelection releaseDateLike(String... value) {
        addLike(FavouritesColumns.RELEASE_DATE, value);
        return this;
    }

    public FavouritesSelection releaseDateContains(String... value) {
        addContains(FavouritesColumns.RELEASE_DATE, value);
        return this;
    }

    public FavouritesSelection releaseDateStartsWith(String... value) {
        addStartsWith(FavouritesColumns.RELEASE_DATE, value);
        return this;
    }

    public FavouritesSelection releaseDateEndsWith(String... value) {
        addEndsWith(FavouritesColumns.RELEASE_DATE, value);
        return this;
    }

    public FavouritesSelection orderByReleaseDate(boolean desc) {
        orderBy(FavouritesColumns.RELEASE_DATE, desc);
        return this;
    }

    public FavouritesSelection orderByReleaseDate() {
        orderBy(FavouritesColumns.RELEASE_DATE, false);
        return this;
    }

    public FavouritesSelection tagline(String... value) {
        addEquals(FavouritesColumns.TAGLINE, value);
        return this;
    }

    public FavouritesSelection taglineNot(String... value) {
        addNotEquals(FavouritesColumns.TAGLINE, value);
        return this;
    }

    public FavouritesSelection taglineLike(String... value) {
        addLike(FavouritesColumns.TAGLINE, value);
        return this;
    }

    public FavouritesSelection taglineContains(String... value) {
        addContains(FavouritesColumns.TAGLINE, value);
        return this;
    }

    public FavouritesSelection taglineStartsWith(String... value) {
        addStartsWith(FavouritesColumns.TAGLINE, value);
        return this;
    }

    public FavouritesSelection taglineEndsWith(String... value) {
        addEndsWith(FavouritesColumns.TAGLINE, value);
        return this;
    }

    public FavouritesSelection orderByTagline(boolean desc) {
        orderBy(FavouritesColumns.TAGLINE, desc);
        return this;
    }

    public FavouritesSelection orderByTagline() {
        orderBy(FavouritesColumns.TAGLINE, false);
        return this;
    }

    public FavouritesSelection popularity(Float... value) {
        addEquals(FavouritesColumns.POPULARITY, value);
        return this;
    }

    public FavouritesSelection popularityNot(Float... value) {
        addNotEquals(FavouritesColumns.POPULARITY, value);
        return this;
    }

    public FavouritesSelection popularityGt(float value) {
        addGreaterThan(FavouritesColumns.POPULARITY, value);
        return this;
    }

    public FavouritesSelection popularityGtEq(float value) {
        addGreaterThanOrEquals(FavouritesColumns.POPULARITY, value);
        return this;
    }

    public FavouritesSelection popularityLt(float value) {
        addLessThan(FavouritesColumns.POPULARITY, value);
        return this;
    }

    public FavouritesSelection popularityLtEq(float value) {
        addLessThanOrEquals(FavouritesColumns.POPULARITY, value);
        return this;
    }

    public FavouritesSelection orderByPopularity(boolean desc) {
        orderBy(FavouritesColumns.POPULARITY, desc);
        return this;
    }

    public FavouritesSelection orderByPopularity() {
        orderBy(FavouritesColumns.POPULARITY, false);
        return this;
    }

    public FavouritesSelection voteAverage(Float... value) {
        addEquals(FavouritesColumns.VOTE_AVERAGE, value);
        return this;
    }

    public FavouritesSelection voteAverageNot(Float... value) {
        addNotEquals(FavouritesColumns.VOTE_AVERAGE, value);
        return this;
    }

    public FavouritesSelection voteAverageGt(float value) {
        addGreaterThan(FavouritesColumns.VOTE_AVERAGE, value);
        return this;
    }

    public FavouritesSelection voteAverageGtEq(float value) {
        addGreaterThanOrEquals(FavouritesColumns.VOTE_AVERAGE, value);
        return this;
    }

    public FavouritesSelection voteAverageLt(float value) {
        addLessThan(FavouritesColumns.VOTE_AVERAGE, value);
        return this;
    }

    public FavouritesSelection voteAverageLtEq(float value) {
        addLessThanOrEquals(FavouritesColumns.VOTE_AVERAGE, value);
        return this;
    }

    public FavouritesSelection orderByVoteAverage(boolean desc) {
        orderBy(FavouritesColumns.VOTE_AVERAGE, desc);
        return this;
    }

    public FavouritesSelection orderByVoteAverage() {
        orderBy(FavouritesColumns.VOTE_AVERAGE, false);
        return this;
    }

    public FavouritesSelection adult(Boolean value) {
        addEquals(FavouritesColumns.ADULT, toObjectArray(value));
        return this;
    }

    public FavouritesSelection orderByAdult(boolean desc) {
        orderBy(FavouritesColumns.ADULT, desc);
        return this;
    }

    public FavouritesSelection orderByAdult() {
        orderBy(FavouritesColumns.ADULT, false);
        return this;
    }

    public FavouritesSelection serializedReviews(String... value) {
        addEquals(FavouritesColumns.SERIALIZED_REVIEWS, value);
        return this;
    }

    public FavouritesSelection serializedReviewsNot(String... value) {
        addNotEquals(FavouritesColumns.SERIALIZED_REVIEWS, value);
        return this;
    }

    public FavouritesSelection serializedReviewsLike(String... value) {
        addLike(FavouritesColumns.SERIALIZED_REVIEWS, value);
        return this;
    }

    public FavouritesSelection serializedReviewsContains(String... value) {
        addContains(FavouritesColumns.SERIALIZED_REVIEWS, value);
        return this;
    }

    public FavouritesSelection serializedReviewsStartsWith(String... value) {
        addStartsWith(FavouritesColumns.SERIALIZED_REVIEWS, value);
        return this;
    }

    public FavouritesSelection serializedReviewsEndsWith(String... value) {
        addEndsWith(FavouritesColumns.SERIALIZED_REVIEWS, value);
        return this;
    }

    public FavouritesSelection orderBySerializedReviews(boolean desc) {
        orderBy(FavouritesColumns.SERIALIZED_REVIEWS, desc);
        return this;
    }

    public FavouritesSelection orderBySerializedReviews() {
        orderBy(FavouritesColumns.SERIALIZED_REVIEWS, false);
        return this;
    }

    public FavouritesSelection serializedTrailers(String... value) {
        addEquals(FavouritesColumns.SERIALIZED_TRAILERS, value);
        return this;
    }

    public FavouritesSelection serializedTrailersNot(String... value) {
        addNotEquals(FavouritesColumns.SERIALIZED_TRAILERS, value);
        return this;
    }

    public FavouritesSelection serializedTrailersLike(String... value) {
        addLike(FavouritesColumns.SERIALIZED_TRAILERS, value);
        return this;
    }

    public FavouritesSelection serializedTrailersContains(String... value) {
        addContains(FavouritesColumns.SERIALIZED_TRAILERS, value);
        return this;
    }

    public FavouritesSelection serializedTrailersStartsWith(String... value) {
        addStartsWith(FavouritesColumns.SERIALIZED_TRAILERS, value);
        return this;
    }

    public FavouritesSelection serializedTrailersEndsWith(String... value) {
        addEndsWith(FavouritesColumns.SERIALIZED_TRAILERS, value);
        return this;
    }

    public FavouritesSelection orderBySerializedTrailers(boolean desc) {
        orderBy(FavouritesColumns.SERIALIZED_TRAILERS, desc);
        return this;
    }

    public FavouritesSelection orderBySerializedTrailers() {
        orderBy(FavouritesColumns.SERIALIZED_TRAILERS, false);
        return this;
    }

    public FavouritesSelection isFavourite(boolean value) {
        addEquals(FavouritesColumns.IS_FAVOURITE, toObjectArray(value));
        return this;
    }

    public FavouritesSelection orderByIsFavourite(boolean desc) {
        orderBy(FavouritesColumns.IS_FAVOURITE, desc);
        return this;
    }

    public FavouritesSelection orderByIsFavourite() {
        orderBy(FavouritesColumns.IS_FAVOURITE, false);
        return this;
    }
}
