package com.futuretraxex.moviemania.utils;

import com.futuretraxex.moviemania.BuildConfig;

/**
 * Created by dragonSlayer on 11/21/2015.
 */
public class Constant {

    public static String IMAGE_W185_URL_SUFFIX = "http://image.tmdb.org/t/p/w185";

    public static String IMAGE_W342_URL_SUFFIX = "http://image.tmdb.org/t/p/w342";

    public static String IMAGE_W500_URL_SUFFIX = "http://image.tmdb.org/t/p/w500";

    public static String API_KEY = BuildConfig.TMDB_API_KEY;

    public static String API_GET_POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular";

    public static String API_GET_TOP_RATED_MOVIES = "http://api.themoviedb.org/3/movie/top_rated";

    /**
     * Movie Detail URL is of the format /movie/{id}
     */
    public static String API_GET_MOVIE_DETAIL = "http://api.themoviedb.org/3/movie";


    public static String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch";

    public static final int FETCH_TYPE_POPULAR = 0;
    public static final int FETCH_TYPE_TOP_RATED = 1;
    public static final int FETCH_TYPE_FAVOURITE = 2;
    public static final int FETCH_TYPE_UPCOMING = 3;

    public static final int GRID_OPTIMAL_WIDTH = 300;
}
