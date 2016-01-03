package com.futuretraxex.moviemania.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Rating;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretraxex.moviemania.Activity.MovieDetailActivity;
import com.futuretraxex.moviemania.Activity.MovieListActivity;
import com.futuretraxex.moviemania.Model.MovieModelDetail;
import com.futuretraxex.moviemania.NetworkServices.NetworkFetchService;
import com.futuretraxex.moviemania.R;
import com.futuretraxex.moviemania.utils.Globals;
import com.futuretraxex.moviemania.utils.Utils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private MovieDetailViewHolder mMovieDetailViewHolder;

    //TODO : On click FAB the movie should be added to a local favourite list (state should toggle and persist).
    //TODO : decide wether to save Reviews and Videos in local storage or not. discuss with Udacity Forum Members.

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

        mGson = new Gson();
        mToolbarColor = getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        //Setup ViewHolder
        mMovieDetailViewHolder = new MovieDetailViewHolder(rootView);

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

        Bundle urlParams = new Bundle();
        urlParams.putString("movie_id",String.valueOf(mId));
        NetworkFetchService.fetchMovieData(urlParams, this);
    }



    private void animateUI()    {
        View[] animatedViews = new View[]   {
                mMovieDetailViewHolder.mMovieTitle,
                mMovieDetailViewHolder.mMovieDateText,
                mMovieDetailViewHolder.mMovieRatingText,
                mMovieDetailViewHolder.mMovieTaglineText,
                mMovieDetailViewHolder.mMovieOverView
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
        final MovieModelDetail movie = mGson.fromJson(movieData, MovieModelDetail.class);
//      final RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.movie_detail_rating);

        //SetupAppBar in case of
        setupAppBar(movie);
        //Populate UI data.
        populateLayout(movie);
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


    private void populateLayout(final MovieModelDetail movie)   {


        Resources res = getResources();
        final String ratingText = String.format(res.getString(R.string.rating_text), movie.vote_average);
        final String tagLineText = String.format(res.getString(R.string.tagline_text), movie.tagline);
        final String dateText = String.format(res.getString(R.string.releast_date_text),Utils.generateUserFriendlyDate(movie.release_date));
        final SpannableStringBuilder tagLineSpanString = new SpannableStringBuilder(tagLineText);
        tagLineSpanString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, tagLineText.length(), 0);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

        MovieDetailViewHolder(View rootView) {
            ButterKnife.bind(this,rootView);

        }
    }
}
