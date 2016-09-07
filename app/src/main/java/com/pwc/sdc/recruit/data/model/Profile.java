package com.pwc.sdc.recruit.data.model;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * @author:dongpo 创建时间: 7/20/2016
 * 描述:
 * 修改:
 */
public class Profile extends ParcelEntity implements Comparable<Profile> {
    public String candidateId;
    public String chineseName;
    public String englishName;
    public String phone;
    public long submitTime;
    public ArrayList<Recruiter> recruiters;
    public String position;
    public String headUrl;
    public String time;
    public int result;




    @Override
    public int compareTo(Profile another) {
        if (another == null && another.submitTime <= 0) {
            return -1;
        }
        if (submitTime > another.submitTime) {
            return -1;
        } else if (submitTime < another.submitTime) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.candidateId);
        dest.writeString(this.chineseName);
        dest.writeString(this.englishName);
        dest.writeString(this.phone);
        dest.writeLong(this.submitTime);
        dest.writeTypedList(this.recruiters);
        dest.writeString(this.position);
        dest.writeString(this.headUrl);
        dest.writeString(this.time);
        dest.writeInt(this.result);
    }

    public Profile() {
    }

    protected Profile(Parcel in) {
        this.candidateId = in.readString();
        this.chineseName = in.readString();
        this.englishName = in.readString();
        this.phone = in.readString();
        this.submitTime = in.readLong();
        this.recruiters = in.createTypedArrayList(Recruiter.CREATOR);
        this.position = in.readString();
        this.headUrl = in.readString();
        this.time = in.readString();
        this.result = in.readInt();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
