package com.futuretraxex.moviemania.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lud on 25-01-2016.
 */
public class MovieTrailerModel implements Parcelable {
    public String id;
    public String iso_639_1;
    public String key;
    public String name;
    public String site;
    public int size;
    public String type;

    protected MovieTrailerModel(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieTrailerModel> CREATOR = new Parcelable.Creator<MovieTrailerModel>() {
        @Override
        public MovieTrailerModel createFromParcel(Parcel in) {
            return new MovieTrailerModel(in);
        }

        @Override
        public MovieTrailerModel[] newArray(int size) {
            return new MovieTrailerModel[size];
        }
    };
}