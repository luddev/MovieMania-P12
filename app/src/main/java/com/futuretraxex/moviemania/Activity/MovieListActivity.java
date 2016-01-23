package com.futuretraxex.moviemania.Activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.futuretraxex.moviemania.Fragment.MovieDetailFragment;
import com.futuretraxex.moviemania.Model.MovieModel;
import com.futuretraxex.moviemania.NetworkServices.NetworkFetchService;
import com.futuretraxex.moviemania.R;
import com.futuretraxex.moviemania.provider.favourites.FavouritesColumns;
import com.futuretraxex.moviemania.provider.favourites.FavouritesCursor;
import com.futuretraxex.moviemania.provider.favourites.FavouritesSelection;
import com.futuretraxex.moviemania.utils.Constant;
import com.futuretraxex.moviemania.utils.EndlessRecyclerOnScrollListener;
import com.futuretraxex.moviemania.utils.Globals;
import com.futuretraxex.moviemania.utils.Utils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements NetworkFetchService.NetworkFetchServiceCB{


    @Bind(R.id.movie_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spinner)
    Spinner mSpinner;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.empty_list_view)
    TextView mEmptyView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    public static String MOVIE_LIST_SAVE_INSTANCE = "movie_list";
    public static String MOVIE_LIST_VIEW_SAVE_INSTANCE = "movie_recycler";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     * Use Global.sTwoPane instead.
     */

    private boolean mTwoPane;
    private int mPage;
    private Gson mGson;
    private boolean mLoading;
    private int mFetchType;
    private EndlessRecyclerOnScrollListener mEndlessScrollListener;

    private Parcelable mRecyclerListState;
    private ArrayList<MovieModel> mMovieAdapterState;

    @OnItemSelected(R.id.spinner)
    void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Bundle urlParams = new Bundle();
        mFetchType = position;
        switch (position) {
            case Constant.FETCH_TYPE_POPULAR:
                ((MovieListAdapter)mRecyclerView.getAdapter()).clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mEndlessScrollListener.reset();
                urlParams.putString("page", "1");
                NetworkFetchService.fetchPopularMovies(urlParams, MovieListActivity.this);
                break;
            case Constant.FETCH_TYPE_TOP_RATED:
                ((MovieListAdapter)mRecyclerView.getAdapter()).clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mEndlessScrollListener.reset();
                urlParams.putString("page", "1");
                NetworkFetchService.fetchTopRatedMovies(urlParams, MovieListActivity.this);
                break;
            case Constant.FETCH_TYPE_FAVOURITE:
                ((MovieListAdapter)mRecyclerView.getAdapter()).clear();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mEndlessScrollListener.reset();
                //Local Fetch
                populateFavouriteList();
                break;
        }
    }

    @OnItemSelected(R.id.spinner)
    void onNothingSelected(AdapterView<?> parent) {

    }

    //TODO : Do this on background thread. -- Done
    private void populateFavouriteList()    {
        //Local Fetch
        new Thread(new Runnable() {
            @Override
            public void run() {
                FavouritesSelection favSelection = new FavouritesSelection();
                favSelection.isFavourite(true).orderByReleaseDate().orderByVoteAverage();
                Cursor c = MovieListActivity.this.getContentResolver().query(FavouritesColumns.CONTENT_URI,
                        null,
                        favSelection.sel(),
                        favSelection.args(),
                        null);
                if(c == null || !c.moveToFirst()) {
                    //Show empty list view, no favourites found.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mRecyclerView.getAdapter().notifyDataSetChanged();
                            mRefreshLayout.setRefreshing(false);
                            if (mRecyclerView.getAdapter().getItemCount() == 0) {
                                mRecyclerView.setVisibility(View.GONE);
                                mEmptyView.setVisibility(View.VISIBLE);
                            } else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mEmptyView.setVisibility(View.GONE);
                            }
                        }
                    });
                    return;
                }
                do {
                    FavouritesCursor favCursor = new FavouritesCursor(c);
                    MovieModel movieModel = new MovieModel(
                            favCursor.getAdult(),
                            favCursor.getPosterPath(),
                            favCursor.getMovieId(),
                            favCursor.getBackdropPath(),
                            favCursor.getOriginalTitle(),
                            favCursor.getOverview(),
                            favCursor.getReleaseDate(),
                            favCursor.getPosterPath(),
                            favCursor.getPopularity(),
                            favCursor.getOriginalTitle(),
                            true,
                            favCursor.getVoteAverage(),
                            0
                    );

                    ((MovieListAdapter)mRecyclerView.getAdapter()).addItem(movieModel);
                }while(c.moveToNext());

                c.close();


                //Update UI.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                        if (mRecyclerView.getAdapter().getItemCount() == 0) {
                            mRecyclerView.setVisibility(View.GONE);
                            mEmptyView.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mEmptyView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //Bind Butterknife.
        ButterKnife.bind(this);
        //Initialize Logger.
        Logger.init();
        //Reload instance state in case of change of orientation.
        if(savedInstanceState != null)  {
//            mRecyclerListState = savedInstanceState.getParcelable(MOVIE_LIST_VIEW_SAVE_INSTANCE);
//            mMovieAdapterState = savedInstanceState.getParcelableArrayList(MOVIE_LIST_SAVE_INSTANCE);

            onRestoreInstanceState(savedInstanceState);
        }
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        mLoading = false;
        mGson = new Gson();

        setupViews();
//        postUICreation();
    }




    @Override
    public void onSuccess(Bundle data) {
        //Construct MovieModels Array using Gson.
        ArrayList<String> movieArrayList = data.getStringArrayList("movie_list");
        for (String movie : movieArrayList) {
            ((MovieListAdapter) mRecyclerView.getAdapter()).addItem(movie);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                if(mRecyclerView.getAdapter().getItemCount() == 0)   {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        });

        mLoading = false;


    }

    @Override
    public void onFailure(Bundle data) {
        mEndlessScrollListener.decrementCurrentPage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(mRecyclerView.getAdapter().getItemCount() == 0)   {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                mRefreshLayout.setRefreshing(false);
            }
        });

        mLoading = false;
        Snackbar.make(this.getWindow().getDecorView().getRootView(), "Something unusual happened", Snackbar.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save Movie Adapter
        mMovieAdapterState = ((MovieListAdapter)mRecyclerView.getAdapter()).getMovieList();
        outState.putParcelableArrayList(MOVIE_LIST_SAVE_INSTANCE, mMovieAdapterState);
        //Save Movie List State
        mRecyclerListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(MOVIE_LIST_VIEW_SAVE_INSTANCE,mRecyclerListState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mRecyclerListState = savedInstanceState.getParcelable(MOVIE_LIST_VIEW_SAVE_INSTANCE);
        mMovieAdapterState = savedInstanceState.getParcelableArrayList(MOVIE_LIST_SAVE_INSTANCE);
    }

//    private void postUICreation()   {
//        if(mMovieAdapterState != null)  {
//            ((MovieListAdapter)mRecyclerView.getAdapter()).resotreArrayList(mMovieAdapterState);
//        }
//        if (mRecyclerListState != null) {
//            mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerListState);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mMovieAdapterState != null)  {
            ((MovieListAdapter)mRecyclerView.getAdapter()).resotreArrayList(mMovieAdapterState);
        }
        if (mRecyclerListState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mRecyclerListState);
        }

    }


    public void setupViews()    {
        final View recyclerView = findViewById(R.id.movie_list);
        //assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        mRecyclerView = (RecyclerView)recyclerView;
        //mSpinner = (Spinner)findViewById(R.id.spinner);
        //mEmptyView = (TextView)findViewById(R.id.emptyListView);
        //mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            Globals.sTwoPane = true;
        }

        //No butterknife binding yet :(
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!mLoading)   {
                    ((MovieListAdapter)mRecyclerView.getAdapter()).clear();
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    mEndlessScrollListener.reset();
                    mLoading = true;
                    refetchData("1");
                }
                else {
                    mRefreshLayout.setRefreshing(false);
                }

            }
        });
    }

    private void refetchData(String page)  {
        //Fetch data, page = 1 Initially
        Bundle urlParams = new Bundle();
        urlParams.putString("page", page);
        switch(mFetchType)  {
            case Constant.FETCH_TYPE_POPULAR:
                NetworkFetchService.fetchPopularMovies(urlParams, MovieListActivity.this);
                break;
            case Constant.FETCH_TYPE_TOP_RATED:
                NetworkFetchService.fetchTopRatedMovies(urlParams, MovieListActivity.this);
                break;
            case Constant.FETCH_TYPE_FAVOURITE:
                populateFavouriteList();
                break;
        }
    }


    /**
     * Recycler View Setup, setup recycler view here including adapter and loading.
     * @param recyclerView RecyclerView instance.
     */

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        //Setup Recycler View
        recyclerView.setAdapter(new MovieListAdapter());
        final GridLayoutManager gridLayoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
        //Create EndlessScrollListener.
        mEndlessScrollListener = generateEndlessScrollListener(gridLayoutManager);
        //Implement Endless Scrolling.
        recyclerView.addOnScrollListener(mEndlessScrollListener);

    }

    /**
     * Generate an EndlessScrollListener for recycler view, as we scroll down we do stuff.
     * @param gridLayoutManager GridLayoutManager instance.
     * @return EndlessRecyclerOnScrollListener
     */
    private EndlessRecyclerOnScrollListener generateEndlessScrollListener(GridLayoutManager gridLayoutManager) {
        return new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                //Show progress bar.
                //Load stuff.
                //Fetch data.
                Bundle urlParams = new Bundle();
                urlParams.putString("page", String.valueOf(current_page));
                mPage = current_page;
                switch(mFetchType)  {
                    case Constant.FETCH_TYPE_POPULAR:
                        NetworkFetchService.fetchPopularMovies(urlParams, MovieListActivity.this);
                        break;
                    case Constant.FETCH_TYPE_TOP_RATED:
                        NetworkFetchService.fetchTopRatedMovies(urlParams, MovieListActivity.this);
                        break;
                }

            }
        };
    }

    /**
     * Thought of moving this into a sperate file in Adapters/MovieListAdapter but that requires
     * passing SupportFragmentManager to the Adapter.
     * Should I do it or not ? ? ?
     *
     * Also this was auto-generated from Master/Detail Activity.
     *
     */

    public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {


        private ArrayList<MovieModel> mValues;

        public MovieListAdapter()  {
            mValues = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }


        public void addItem(String movieModelGsonString)    {
            mValues.add(mGson.fromJson(movieModelGsonString, MovieModel.class));
        }

        public void addItem(MovieModel movieModel)  {
            mValues.add(movieModel);
        }

        public ArrayList<MovieModel> getMovieList()  {
            return mValues;
        }

        public void resotreArrayList(ArrayList<MovieModel> movieList)   {
            mValues = movieList;
        }

        @Override
        public long getItemId(int position) {
            return mValues.get(position).id;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
            holder.mProgressBar.setVisibility(ProgressBar.VISIBLE);

            //TODO : Show a favourite icon if the 'id' (movie_id) is present in favourites list :).
            //TODO : On clicking that fav icon the favourites state should toggle on that list item and should be updated in database.

            Picasso.with(MovieListActivity.this)
                    .load(Utils.generateW342ImageUri(holder.mItem.poster_path))
                    .fit()
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.mContentView, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            //holder.mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                            holder.mContentView.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
                        }
                    });

            holder.mView.setOnClickListener(generateOnItemClickListener(holder));
        }

        /**
         * Generate onclick listener for each item.
         * @param holder ViewHolder.
         * @return View.OnClickListener a new OnClickListener
         */
        private View.OnClickListener generateOnItemClickListener(final ViewHolder holder)  {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Globals.sTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putLong(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_TITLE, holder.mItem.original_title);

                        //Scale up animation balantly copied from
                        //http://vickychijwani.me/udacity-android-nanodegree-week-2/
                        ActivityOptions opts;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            opts = ActivityOptions.makeScaleUpAnimation(
                                    // the source view from which to animate the new Activity
                                    // defines the co-ordinate space for initial (x, y) location
                                    v,
                                    // starting (x, y) position for animation
                                    // NOTE: these co-ordinates are relative to the source view above
                                    0, 0,
                                    // initial width and height of the new Activity
                                    v.getWidth(), v.getHeight());
                            context.startActivity(intent, opts.toBundle());
                        }
                        else {
                            context.startActivity(intent);
                        }
                    }
                }
            };
        }

        public void clear() {
            mValues.clear();
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;

            @Bind(R.id.content)
            public ImageView mContentView;
            @Bind(R.id.progress_bar_image)
            public ProgressBar mProgressBar;

            public MovieModel mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this,view);
                mContentView = (ImageView) view.findViewById(R.id.content);
                mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_image);
            }

        }

    }
}
