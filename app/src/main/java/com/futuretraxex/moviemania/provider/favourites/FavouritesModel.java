package com.futuretraxex.moviemania.provider.favourites;

import com.futuretraxex.moviemania.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * List of favourites movie.
 */
public interface FavouritesModel extends BaseModel {

    /**
     * Unique TMDB Movie id
     */
    long getMovieId();

    /**
     * Get the {@code original_title} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getOriginalTitle();

    /**
     * Get the {@code overview} value.
     * Cannot be {@code null}.
     */
    @NonNull
    String getOverview();

    /**
     * Get the {@code backdrop_path} value.
     * Can be {@code null}.
     */
    @Nullable
    String getBackdropPath();

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterPath();

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    String getReleaseDate();

    /**
     * Get the {@code tagline} value.
     * Can be {@code null}.
     */
    @Nullable
    String getTagline();

    /**
     * Get the {@code popularity} value.
     * Can be {@code null}.
     */
    @Nullable
    Float getPopularity();

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    Float getVoteAverage();

    /**
     * Boolean wether movie is adult rated or not
     * Can be {@code null}.
     */
    @Nullable
    Boolean getAdult();

    /**
     * Get the {@code serialized_reviews} value.
     * Can be {@code null}.
     */
    @Nullable
    String getSerializedReviews();

    /**
     * Get the {@code serialized_trailers} value.
     * Can be {@code null}.
     */
    @Nullable
    String getSerializedTrailers();

    /**
     * Get the {@code is_favourite} value.
     */
    boolean getIsFavourite();
}
