package com.futuretraxex.moviemania.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuretraxex.moviemania.Model.MovieReviewModel;
import com.futuretraxex.moviemania.R;
import com.orhanobut.logger.Logger;

/**
 * Created by lud on 25-01-2016.
 *
 * http://android-developers.blogspot.in/2011/08/horizontal-view-swiping-with-viewpager.html
 */

public class ReviewPagerAdapter extends PagerAdapter {


    private MovieReviewModel mMovieReviews[];
    private Context mContext;

    public ReviewPagerAdapter(Context _context, MovieReviewModel[] movieReviews)    {
        super();
        mContext = _context;
        mMovieReviews = movieReviews;
    }

    @Override
    public int getCount() {
        return mMovieReviews.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final TextView reviewText = (TextView) layoutInflater.inflate(R.layout.review_list_content,container,false);
        reviewText.setText(mMovieReviews[position].content);
        container.addView(reviewText,0);
        //Logger.w("Adding Object With Content : " + mMovieReviews[position].content.substring(0,4));


        reviewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(Intent.ACTION_VIEW);
                //reviewIntent.addCategory(Intent.CATEGORY_APP_BROWSER);
                reviewIntent.setData(Uri.parse(mMovieReviews[position].url));
                mContext.startActivity(reviewIntent);
            }
        });

        return reviewText;
        //return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }
}
