package com.futuretraxex.moviemania.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by dragonSlayer on 11/21/2015.
 */
public class Utils {

    public static Uri generateW185ImageUri(String imageLocation)  {
        return Uri.parse(Constant.IMAGE_W185_URL_SUFFIX).buildUpon()
                .appendEncodedPath(imageLocation)
                .build();

    }

    public static Uri generateW342ImageUri(String imageLocation)  {
        return Uri.parse(Constant.IMAGE_W342_URL_SUFFIX).buildUpon()
                .appendEncodedPath(imageLocation)
                .build();

    }

    public static Uri generateW500ImageUri(String imageLocation)  {
        return Uri.parse(Constant.IMAGE_W500_URL_SUFFIX).buildUpon()
                .appendEncodedPath(imageLocation)
                .build();

    }

    public static String generateUserFriendlyDate(String date)  {
        return getFriendlyMonth(date) + " " + getDay(date) + ", " + getYear(date);
    }

    public static String getFriendlyMonth(String date)  {
        switch(Integer.valueOf(date.split("-")[1]))  {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return "";
    }

    public static String getYear(String date)   {
        return date.split("-")[0];
    }

    public static String getDay(String date)    {
        return date.split("-")[2];
    }

    public static boolean isLandscape(Context ctx)  {
        return ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int getScreenWidth(Context context)  {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;

    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    public static boolean getNetworkConnectivity(Context context)   {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static Uri generateYoutubeMQThumbnail(String youtubeId)  {
        return Uri.parse(Constant.API_YOUTUBE_IMAGE_BASE_URL)
                .buildUpon()
                .appendEncodedPath(youtubeId)
                .appendEncodedPath(Constant.API_YOUTUBE_MQ_IMAGE_CODE)
                .build();
    }


    /**
     * Generates youtube uri of the form https://www.youtube.com/watch?v={id}
     * @param id Youtube Video Id
     * @return Youtube Video uri.
     */
    public static Uri generateYoutubeUri(String id) {
        return Uri.parse(Constant.YOUTUBE_VIDEO_URL)
                .buildUpon()
                .appendQueryParameter("v",id)
                .build();
    }

}
