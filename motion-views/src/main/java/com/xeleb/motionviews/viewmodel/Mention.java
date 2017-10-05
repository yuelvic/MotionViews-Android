package com.xeleb.motionviews.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mention implements Parcelable {

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("fullName")
    @Expose
    private String fullname;

    public Mention() {}

    protected Mention(Parcel in) {
        nickname = in.readString();
        fullname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(fullname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mention> CREATOR = new Creator<Mention>() {
        @Override
        public Mention createFromParcel(Parcel in) {
            return new Mention(in);
        }

        @Override
        public Mention[] newArray(int size) {
            return new Mention[size];
        }
    };

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public static Creator<Mention> getCREATOR() {
        return CREATOR;
    }
}