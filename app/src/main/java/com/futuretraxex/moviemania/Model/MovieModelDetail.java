package com.futuretraxex.moviemania.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dragonSlayer on 11/23/2015.
 */
public class MovieModelDetail implements Parcelable {

    public Boolean adult;
    public String backdrop_path;
    public long id;
    public String original_language;
    public String original_title;
    public String overview;
    public String release_date;
    public String poster_path;
    public float popularity;
    public String tagline;
    public String title;
    public Boolean video;
    public float vote_average;
    public int vote_count;

    protected MovieModelDetail(Parcel in) {
        byte adultVal = in.readByte();
        adult = adultVal == 0x02 ? null : adultVal != 0x00;
        backdrop_path = in.readString();
        id = in.readLong();
        original_language = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        poster_path = in.readString();
        popularity = in.readFloat();
        tagline = in.readString();
        title = in.readString();
        byte videoVal = in.readByte();
        video = videoVal == 0x02 ? null : videoVal != 0x00;
        vote_average = in.readFloat();
        vote_count = in.readInt();
    }

    public MovieModelDetail()   {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (adult == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (adult ? 0x01 : 0x00));
        }
        dest.writeString(backdrop_path);
        dest.writeLong(id);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(poster_path);
        dest.writeFloat(popularity);
        dest.writeString(tagline);
        dest.writeString(title);
        if (video == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (video ? 0x01 : 0x00));
        }
        dest.writeFloat(vote_average);
        dest.writeInt(vote_count);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieModelDetail> CREATOR = new Parcelable.Creator<MovieModelDetail>() {
        @Override
        public MovieModelDetail createFromParcel(Parcel in) {
            return new MovieModelDetail(in);
        }

        @Override
        public MovieModelDetail[] newArray(int size) {
            return new MovieModelDetail[size];
        }
    };
}