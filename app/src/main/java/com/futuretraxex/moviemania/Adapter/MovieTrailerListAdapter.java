package com.futuretraxex.moviemania.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.futuretraxex.moviemania.Activity.MovieDetailActivity;
import com.futuretraxex.moviemania.Fragment.MovieDetailFragment;
import com.futuretraxex.moviemania.Model.MovieTrailerModel;
import com.futuretraxex.moviemania.R;
import com.futuretraxex.moviemania.utils.Globals;
import com.futuretraxex.moviemania.utils.Utils;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lud on 25-01-2016.
 */
public class MovieTrailerListAdapter extends RecyclerView.Adapter<MovieTrailerListAdapter.ViewHolder> {

    private MovieTrailerModel[] mMovieTrailers;
    private Context mContext;
    public MovieTrailerListAdapter(Context context, MovieTrailerModel[] moviesTrailer)    {
        super();
        mContext = context;
        mMovieTrailers = moviesTrailer;
        for(int i = 0 ; i < mMovieTrailers.length ; i++)    {
            Logger.w("Movie Trailer with YT ID : " + mMovieTrailers[i].key);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mMovieTrailer = mMovieTrailers[position];
        Logger.w("Binding View For Video : " + holder.mMovieTrailer.key);
        holder.mProgressBar.setVisibility(ProgressBar.VISIBLE);
        Picasso.with(mContext)
                .load(Utils.generateYoutubeMQThumbnail(holder.mMovieTrailer.key))
                .fit()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(holder.mContentView);
        holder.mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        holder.mView.setOnClickListener(generateOnItemClickListener(holder));
        holder.mPlayButton.setOnClickListener(generateOnItemClickListener(holder));
        holder.mContentView.setOnClickListener(generateOnItemClickListener(holder));
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
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
                youtubeIntent.setData(Utils.generateYoutubeUri(holder.mMovieTrailer.key));
                mContext.startActivity(youtubeIntent);
            }
        };
    }



    public void clear() {
        mMovieTrailers = null;
    }

    @Override
    public int getItemCount() {
        return mMovieTrailers.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public final View mView;

        MovieTrailerModel mMovieTrailer;

        @Bind(R.id.movie_detail_trailer)
        public ImageView mContentView;
        @Bind(R.id.progress_bar_image)
        public ProgressBar mProgressBar;
        @Bind(R.id.play_button)
        public ImageView mPlayButton;

        public ViewHolder(View rootView) {
            super(rootView);
            mView = rootView;
            ButterKnife.bind(this,rootView);
        }

    }
}
