package com.futuretraxex.moviemania.Activity;

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.futuretraxex.moviemania.Fragment.MovieDetailFragment;
import com.futuretraxex.moviemania.Model.MovieModelDetail;
import com.futuretraxex.moviemania.NetworkServices.NetworkFetchService;
import com.futuretraxex.moviemania.R;
import com.futuretraxex.moviemania.provider.favourites.FavouritesColumns;
import com.futuretraxex.moviemania.provider.favourites.FavouritesContentValues;
import com.futuretraxex.moviemania.provider.favourites.FavouritesCursor;
import com.futuretraxex.moviemania.provider.favourites.FavouritesSelection;
import com.futuretraxex.moviemania.utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {


    private long mMovieID;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        //toolbar.setTitle(getIntent().getStringExtra(MovieDetailFragment.ARG_ITEM_TITLE));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //Bind UI.
        ButterKnife.bind(this);
//        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mMovieID = getIntent().getLongExtra(MovieDetailFragment.ARG_ITEM_ID,0);

        //Using Butterknife Instead.
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if(Utils.isLandscape(this)) {
            //final CollapsingToolbarLayout collapsedAppBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            //collapsedAppBar.setScrimsShown(false);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(MovieDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(MovieDetailFragment.ARG_ITEM_ID,0));


            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
        setFabIcon();
    }


    //On Fav icon click, favourite if it isn't in favourite list , unfavourite otherwise.
    @OnClick(R.id.fab)
    public void onFabClickHandler(View view)    {
        final Gson gson = new Gson();

        FavouritesSelection where = new FavouritesSelection();
        where.movieId(mMovieID);
        FavouritesCursor favouriteItem = where.query(getContentResolver());
        //If movie is in database check if it is favourite or not.
        if(favouriteItem != null && favouriteItem.moveToFirst())    {
            //if it is favourite, unFavourite it.
//            FavouritesContentValues values = new FavouritesContentValues();
//            values.putMovieId(mMovieID);
//            if(favouriteItem.getIsFavourite())  {
//                values.putIsFavourite(false);
//                Snackbar.make(view, "Removed from Favourites", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//            //if it is not favourite, Favourite it.
//            else {
//                Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                values.putIsFavourite(true);
//
//            }
            Uri movieUri = ContentUris.withAppendedId(FavouritesColumns.CONTENT_URI,favouriteItem.getId());
            getContentResolver().delete(movieUri,null,null);
            //Use this instead.
//                String WHERE = FavouritesColumns.MOVIE_ID + "= ?";
//                getContentResolver().update(FavouritesColumns.CONTENT_URI,values.values(),WHERE,new String[] {new Long(favouriteItem.getMovieId()).toString()});
            //Update Fav icon.
            setFabIcon();
        }
        else {
            //if it isn't in database add it and favourite it.
            final FavouritesContentValues values = new FavouritesContentValues();
            values.putIsFavourite(true);
            Bundle inData = new Bundle();
            Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            inData.putString("movie_id",new Long(mMovieID).toString());
            NetworkFetchService.fetchMovieData(inData, new NetworkFetchService.NetworkFetchServiceCB() {
                @Override
                public void onSuccess(Bundle data) {
                    MovieModelDetail movieData = gson.fromJson(data.getString("movie_data"),MovieModelDetail.class);
                    values.putAdult(movieData.adult);
                    values.putMovieId(mMovieID);
                    values.putOriginalTitle(movieData.original_title);
                    values.putOverview(movieData.overview);
                    values.putPopularity(movieData.popularity);
                    values.putPosterPath(movieData.poster_path);
                    values.putTagline(movieData.tagline);
                    values.putVoteAverage(movieData.vote_average);
                    values.putReleaseDate(movieData.release_date);
                    values.putBackdropPath(movieData.backdrop_path);
                    //Insert into table.
                    MovieDetailActivity.this.getContentResolver().insert(FavouritesColumns.CONTENT_URI,
                            values.values());
                    //Update UI.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setFabIcon();
                        }
                    });
                }

                @Override
                public void onFailure(Bundle data) {
                    //Failed to update db and UI.
                }
            });
            //Set FAB Icon.
        }

    }

    private void setFabIcon()   {
        long itemID = mMovieID;

        FavouritesSelection where = new FavouritesSelection();
        where.movieId(itemID);
        FavouritesCursor favouriteItem = where.query(getContentResolver());

        if(favouriteItem != null && favouriteItem.moveToFirst()) {
            if(favouriteItem.getIsFavourite())  {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mFab.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp));
                }
                else {
                    mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mFab.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
                else {
                    mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mFab.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp));
            }
            else {
                mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(Utils.isLandscape(this)) {
            final CollapsingToolbarLayout collapsedAppBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            collapsedAppBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
