package com.futuretraxex.moviemania.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.futuretraxex.moviemania.Activity.MovieDetailActivity;
import com.futuretraxex.moviemania.Activity.MovieListActivity;
import com.futuretraxex.moviemania.Adapter.MovieTrailerListAdapter;
import com.futuretraxex.moviemania.Adapter.ReviewPagerAdapter;
import com.futuretraxex.moviemania.Model.MovieModelDetail;
import com.futuretraxex.moviemania.Model.MovieReviewModel;
import com.futuretraxex.moviemania.Model.MovieTrailerModel;
import com.futuretraxex.moviemania.NetworkServices.NetworkFetchService;
import com.futuretraxex.moviemania.R;
import com.futuretraxex.moviemania.provider.favourites.FavouritesColumns;
import com.futuretraxex.moviemania.provider.favourites.FavouritesContentValues;
import com.futuretraxex.moviemania.provider.favourites.FavouritesCursor;
import com.futuretraxex.moviemania.provider.favourites.FavouritesSelection;
import com.futuretraxex.moviemania.utils.Globals;
import com.futuretraxex.moviemania.utils.PagerTransforms.ZoomOutPageTransformer;
import com.futuretraxex.moviemania.utils.Utils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements NetworkFetchService.NetworkFetchServiceCB{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_ITEM_TITLE = "item_title";

    private long mId;

    private Gson mGson;

    private int mToolbarColor;

    private int mCurrentReview;

    private MovieDetailViewHolder mMovieDetailViewHolder;

    private Timer mTimer = null;

    private Bundle mMovieData;

    //Bind floating action button here.
    @Bind(R.id.fab)
    public FloatingActionButton mLikeButton;


    private NestedScrollView mMovieDetailContainer;

    //TODO : decide wether to save Reviews and Videos in local storage or not. discuss with Udacity Forum Members.
    //TODO : Make favourites work when we are offline.

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mId = getArguments().getLong(ARG_ITEM_ID);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                //appBarLayout.setTitle(mItem.content);
            }
        }
        if(!Globals.sTwoPane) {
            mMovieDetailContainer = (NestedScrollView) getActivity().findViewById(R.id.movie_detail_container);
            mMovieDetailContainer.setSmoothScrollingEnabled(true);
        }

        mGson = new Gson();
        mToolbarColor = getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        //Setup ViewHolder
        mMovieDetailViewHolder = new MovieDetailViewHolder(rootView);
//        mMovieDetailViewHolder.mMovieReviews = (TextSwitcher)rootView.findViewById(R.id.movie_detail_reviews);
        //mMovieDetailViewHolder.mMovieReviews.setCurrentText("Test");
        ButterKnife.bind(this,rootView);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mId = getArguments().getLong(ARG_ITEM_ID);
        }


        if(Utils.getNetworkConnectivity(getActivity())) {
            NetworkFetchService.fetchMovieDataReviewandTrailer(mId, this);
        }
        else {
            Bundle data = new Bundle();
            FavouritesSelection where = new FavouritesSelection();
            where.movieId(mId);
            FavouritesCursor favouriteItem = where.query(getActivity().getContentResolver());

            if(favouriteItem !=null && favouriteItem.moveToFirst()) {
                MovieModelDetail movieData = new MovieModelDetail();
                movieData.adult = favouriteItem.getAdult();
                movieData.poster_path = favouriteItem.getPosterPath();
                movieData.title = favouriteItem.getOriginalTitle();
                movieData.original_title = favouriteItem.getOriginalTitle();
                movieData.vote_average = favouriteItem.getVoteAverage();
                movieData.overview = favouriteItem.getOverview();
                movieData.release_date = favouriteItem.getReleaseDate();
                movieData.popularity = favouriteItem.getPopularity();
                movieData.tagline = favouriteItem.getTagline();
                movieData.id = favouriteItem.getMovieId();
                movieData.backdrop_path = favouriteItem.getBackdropPath();


                String movieString = mGson.toJson(movieData,MovieModelDetail.class);

                data.putString("trailer_data",favouriteItem.getSerializedTrailers());
                data.putString("review_data",favouriteItem.getSerializedReviews());
                data.putString("movie_data",movieString);
                mMovieData = new Bundle(data);
                onSuccess(data);
            }

        }

        //Fetch Reviews and trailers.
        //setup Like button here.
        if(Globals.sTwoPane)    {
            FavouritesSelection where = new FavouritesSelection();
            where.movieId(mId).and().isFavourite(true);
            FavouritesCursor favouriteItem = where.query(getActivity().getContentResolver());
            //Set floating bar icon depending on wether current movie is in favourite list or not.
            if(favouriteItem != null && favouriteItem.moveToFirst())    {
                setFabIcon(true);
            }
            else {
                setFabIcon(false);
            }
        }
    }

    //On Fav icon click, favourite if it isn't in favourite list , unfavourite otherwise.
    @OnClick(R.id.fab)
    public void onFabClickHandler(View view)    {
        final Gson gson = new Gson();

        FavouritesSelection where = new FavouritesSelection();
        where.movieId(mId);
        FavouritesCursor favouriteItem = where.query(getActivity().getContentResolver());
        //If movie is in database check if it is favourite or not.
        if(favouriteItem != null && favouriteItem.moveToFirst())    {
//            if it is favourite, unFavourite it.
            FavouritesContentValues values = new FavouritesContentValues();
            values.putMovieId(mId);
            if(favouriteItem.getIsFavourite())  {
                values.putIsFavourite(false);
                Snackbar.make(view, "Removed from Favourites", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                setFabIcon(false);

            }
            //if it is not favourite, Favourite it.
            else {
                Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                values.putIsFavourite(true);
                setFabIcon(true);

            }
            Uri movieUri = ContentUris.withAppendedId(FavouritesColumns.CONTENT_URI,favouriteItem.getId());
            getActivity().getContentResolver().update(movieUri,values.values(),null,null);
//            getContentResolver().delete(movieUri,null,null);
            //Use this instead.
//                String WHERE = FavouritesColumns.MOVIE_ID + "= ?";
//            getContentResolver().update(FavouritesColumns.CONTENT_URI,values.values(),WHERE,new String[] {new Long(favouriteItem.getMovieId()).toString()});
            //Update Fav icon.

        }
        else {
            //if it isn't in database add it and favourite it.
            final FavouritesContentValues values = new FavouritesContentValues();
            values.putIsFavourite(true);
            Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setFabIcon(true);
                }
            });
            Logger.w("Adding to favourites new entry");
            if(mMovieData != null)  {
                Logger.w("Adding to favourites");
                MovieModelDetail movieData = gson.fromJson(mMovieData.getString("movie_data"),MovieModelDetail.class);
                values.putAdult(movieData.adult);
                values.putMovieId(mId);
                values.putOriginalTitle(movieData.original_title);
                values.putOverview(movieData.overview);
                values.putPopularity(movieData.popularity);
                values.putPosterPath(movieData.poster_path);
                values.putTagline(movieData.tagline);
                values.putVoteAverage(movieData.vote_average);
                values.putReleaseDate(movieData.release_date);
                values.putBackdropPath(movieData.backdrop_path);
                //Insert into table.
                values.putSerializedTrailers(mMovieData.getString("trailer_data"));
                values.putSerializedReviews(mMovieData.getString("review_data"));
                getActivity().getContentResolver().insert(FavouritesColumns.CONTENT_URI,values.values());

            }
            //Don't need to fetch it twice.
//
//            NetworkFetchService.fetchMovieDataReviewandTrailer(mId, new NetworkFetchService.NetworkFetchServiceCB() {
//                @Override
//                public void onSuccess(Bundle data) {
//
//
//                }
//
//                @Override
//                public void onFailure(Bundle data) {
//                    //Failed to update db and UI.
//                }
//            });
            //Set FAB Icon.
        }

    }

    private void setFabIcon(boolean isFavourite)   {
        long itemID = mId;

        if(isFavourite) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mLikeButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_black_24dp));
            }
            else {
                mLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mLikeButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_favorite_border_black_24dp));
            }
            else {
                mLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
            }
        }
    }

    private void animateUI()    {
        View[] animatedViews = new View[]   {
                mMovieDetailViewHolder.mMovieTitle,
                mMovieDetailViewHolder.mMovieDateText,
                mMovieDetailViewHolder.mMovieRatingText,
                mMovieDetailViewHolder.mMovieTaglineText,
                mMovieDetailViewHolder.mMovieOverView,
                mMovieDetailViewHolder.mMovieTrailers,
                mMovieDetailViewHolder.mMovieReviewViewPager

        };

        Interpolator interpolator = new DecelerateInterpolator();

        for (int i = 0; i < animatedViews.length; ++i) {
            View v = animatedViews[i];

            // let's enable hardware acceleration for better performance
            // http://blog.danlew.net/2015/10/20/using-hardware-layers-to-improve-animation-performance/
            v.setLayerType(View.LAYER_TYPE_HARDWARE, null);

            // initial state: hide the view and move it down slightly
            v.setAlpha(0f);
            v.setTranslationY(75);

            v.animate()
                    .setInterpolator(interpolator)
                    .alpha(1.0f)
                    .translationY(0)
                    // this little calculation here produces the staggered effect we
                    // saw, so each animation starts a bit after the previous one
                    .setStartDelay(100 + 75 * i)
                    .start();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onSuccess(Bundle data) {
        String movieData = data.getString("movie_data");
        mMovieData = new Bundle(data);
        final MovieModelDetail movie = mGson.fromJson(movieData, MovieModelDetail.class);
//      final RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.movie_detail_rating);
        try {
            JSONArray movieReviewsJSON = new JSONArray(data.getString("review_data"));
            JSONArray movieTrailersJSON = new JSONArray(data.getString("trailer_data"));
            MovieReviewModel movieReviews[] = new MovieReviewModel[movieReviewsJSON.length()];
            MovieTrailerModel movieTrailers[] = new MovieTrailerModel[movieTrailersJSON.length()];

            for(int i = 0 ; i < movieTrailersJSON.length(); i++)    {
                movieTrailers[i] = mGson.fromJson(movieTrailersJSON.getJSONObject(i).toString(),MovieTrailerModel.class);
            }
            for(int i = 0 ; i < movieReviewsJSON.length(); i++)    {
                movieReviews[i] = mGson.fromJson(movieReviewsJSON.getJSONObject(i).toString(),MovieReviewModel.class);
                movieReviews[i].content = movieReviews[i].content.substring(0,movieReviews[i].content.length() > 70 ? 70 : movieReviews[i].content.length()) + "... (tap to read more)";
            }
            //SetupAppBar in case of
            setupAppBar(movie);
            //Populate UI data.
            if(!isDetached()) {
                populateLayout(movie, movieReviews, movieTrailers);
            }
        }
        catch(JSONException jox)    {
            Logger.e(jox.toString());
        }

    }


    @Override
    public void onFailure(Bundle data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getView(), "Something unusual happened", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private void populateLayout(final MovieModelDetail movie, final MovieReviewModel[] movieReviewModels, final MovieTrailerModel[] movieTrailerModels)   {


        final Resources res = getResources();
        final String ratingText = String.format(res.getString(R.string.rating_text), movie.vote_average);
        final String tagLineText = String.format(res.getString(R.string.tagline_text), movie.tagline);
        final String dateText = String.format(res.getString(R.string.releast_date_text),Utils.generateUserFriendlyDate(movie.release_date));
        final SpannableStringBuilder tagLineSpanString = new SpannableStringBuilder(tagLineText);
        tagLineSpanString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, tagLineText.length(), 0);





        mCurrentReview = 0;


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(movieReviewModels.length > 0)    {
                    mMovieDetailViewHolder.mMovieDetailReview.setVisibility(View.VISIBLE);
                    mMovieDetailViewHolder.mMovieReviewViewPager.setAdapter(new ReviewPagerAdapter(getActivity(),movieReviewModels));
                    mMovieDetailViewHolder.mMovieReviewViewPager.getAdapter().notifyDataSetChanged();
                }
                else {
                    mMovieDetailViewHolder.mMovieDetailReview.setVisibility(View.GONE);
                }

                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mMovieDetailViewHolder.mMovieTrailers.getLayoutParams();
                //calculate height according to our use case nad rescale Trailer View.
                int heightInDP;
                if(movieTrailerModels.length == 0)  {
                    heightInDP = 0;
                }
                else if (movieTrailerModels.length == 1 || movieTrailerModels.length == 2)    {
                    heightInDP = 180;
                }
                else {
                    if(movieTrailerModels.length % 2 == 1)  {
                        heightInDP = (( movieTrailerModels.length / 2) + 1)  * 180;
                    }
                    else {
                        heightInDP = ( movieTrailerModels.length / 2)  * 180;
                    }

                }

                int heightInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInDP, getResources().getDisplayMetrics());
                layoutParams.height = heightInPixels;
                mMovieDetailViewHolder.mMovieTrailers.setLayoutParams(layoutParams);
                mMovieDetailViewHolder.mMovieTrailers.setAdapter(new MovieTrailerListAdapter(getActivity(),movieTrailerModels));
                mMovieDetailViewHolder.mMovieTrailers.getAdapter().notifyDataSetChanged();
                if(!Globals.sTwoPane)    {
                    mMovieDetailContainer.setVerticalScrollbarPosition(0);
                }

                mMovieDetailViewHolder.mMovieReviewViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

                if(movieReviewModels != null || movieReviewModels.length > 0)   {
//                    mMovieDetailViewHolder.mMovieReviews.setCurrentText(movieReviewModels[mCurrentReview].content);
                    mCurrentReview++;
                    if(mCurrentReview > movieReviewModels.length - 1)   {
                        mCurrentReview = 0;
                    }
                }

                mMovieDetailViewHolder.mMovieRatingText.setText(ratingText);
                if (movie.tagline.length() == 0) {
                    mMovieDetailViewHolder.mMovieTaglineText.setVisibility(View.GONE);
                } else {
                    mMovieDetailViewHolder.mMovieTaglineText.setText(tagLineSpanString);
                }
                mMovieDetailViewHolder.mMovieOverView.setText(movie.overview);
                mMovieDetailViewHolder.mMovieTitle.setText(movie.original_title);
                mMovieDetailViewHolder.mMovieDateText.setText(dateText);
                Picasso.with(getActivity())
                        .load(Utils.generateW342ImageUri(movie.poster_path))
                        .error(R.drawable.ic_broken_image_black_24dp)
                        .fit()
                        .into(mMovieDetailViewHolder.mMoviePoster);
                animateUI();


            }
        });


        if(movieReviewModels != null || movieReviewModels.length > 0)   {
            mTimer = new Timer("update-reviews");
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMovieDetailViewHolder.mMovieReviewViewPager.setCurrentItem(mCurrentReview);
                            mCurrentReview++;
                            if(mCurrentReview > movieReviewModels.length - 1)   {
                                mCurrentReview = 0;
                            }
                        }
                    });
                }
            },4000,4000);
        }


    }

    private void setupAppBar(final MovieModelDetail movie)  {
        final AppBarLayout appBar;
        final CollapsingToolbarLayout collapsedAppBar;
        //Setup appbar if it isn't two pane.
        if(!Globals.sTwoPane)    {

            try {
                appBar = (AppBarLayout)getActivity().findViewById(R.id.app_bar);
            }
            catch(NullPointerException nox) {
                Logger.w("Error ?");
                return;
            }

            /**
             *  below code is for setting up app bar image and color on retraction.
             *  don't need these things on landscape layout.
             * */

            if(!Utils.isLandscape(getActivity()))    {

                collapsedAppBar = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
                appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if(verticalOffset > -200) {
                            collapsedAppBar.setTitleEnabled(false);
                        }
                        else {
                            collapsedAppBar.setTitleEnabled(true);
                            collapsedAppBar.setTitle(movie.original_title);
                        }
                    }
                });

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(getActivity())
                                .load(Utils.generateW342ImageUri(movie.backdrop_path))
                                .into(new Target() {
                                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                                        Palette p = Palette.from(bitmap).generate();
                                        int statusBarColor = p.getDarkVibrantColor(mToolbarColor);
                                        int toolbarColor = p.getDarkVibrantColor(mToolbarColor);
                                        mToolbarColor = Color.argb(220, Color.red(toolbarColor), Color.green(toolbarColor), Color.blue(toolbarColor));
                                        statusBarColor = Color.argb(255, Color.red(statusBarColor), Color.green(statusBarColor), Color.blue(statusBarColor));
                                        collapsedAppBar.setStatusBarScrimColor(statusBarColor);
                                        collapsedAppBar.setContentScrimColor(mToolbarColor);
                                        collapsedAppBar.setBackground(new BitmapDrawable(bitmap));

                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable drawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable drawable) {

                                    }
                                });
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mTimer != null)  {
            mTimer.cancel();
        }

    }

    public static class MovieDetailViewHolder {
        @Bind(R.id.movie_detail_overview)
        public TextView mMovieOverView;
        @Bind(R.id.movie_detail_title)
        public TextView mMovieTitle;
        @Bind(R.id.movie_detail_rating_text)
        public TextView mMovieRatingText;
        @Bind(R.id.movie_detail_tagline_text)
        public TextView mMovieTaglineText;
        @Bind(R.id.movie_detail_date)
        public TextView mMovieDateText;
        @Bind(R.id.movie_detail_poster)
        public ImageView mMoviePoster;
        @Bind(R.id.trailers)
        public RecyclerView mMovieTrailers;
        @Bind(R.id.movie_review_viewpager)
        public ViewPager mMovieReviewViewPager;
        @Bind(R.id.movie_detail_review_text)
        public TextView mMovieDetailReview;


        MovieDetailViewHolder(View rootView) {
            ButterKnife.bind(this,rootView);

        }
    }
}
