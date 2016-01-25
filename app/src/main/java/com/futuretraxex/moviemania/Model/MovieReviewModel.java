package com.futuretraxex.moviemania.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lud on 25-01-2016.
 */
public class MovieReviewModel implements Parcelable {
    public String id;
    public String author;
    public String content;
    public String url;

    protected MovieReviewModel(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieReviewModel> CREATOR = new Parcelable.Creator<MovieReviewModel>() {
        @Override
        public MovieReviewModel createFromParcel(Parcel in) {
            return new MovieReviewModel(in);
        }

        @Override
        public MovieReviewModel[] newArray(int size) {
            return new MovieReviewModel[size];
        }
    };
}