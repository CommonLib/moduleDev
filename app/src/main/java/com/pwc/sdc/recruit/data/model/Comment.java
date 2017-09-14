package com.pwc.sdc.recruit.data.model;

import android.os.Parcel;

/**
 * @author:dongpo 创建时间: 7/25/2016
 * 描述:
 * 修改:
 */
public class Comment extends ParcelEntity {
    public String candidateId;
    public String comment;
    public int result;
    public String interViewTime;
    public String recruiterName;
    public String guid;
    public String level;
    public long commentTime;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.candidateId);
        dest.writeString(this.comment);
        dest.writeInt(this.result);
        dest.writeString(this.interViewTime);
        dest.writeString(this.recruiterName);
        dest.writeString(this.guid);
        dest.writeString(this.level);
        dest.writeLong(this.commentTime);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.candidateId = in.readString();
        this.comment = in.readString();
        this.result = in.readInt();
        this.interViewTime = in.readString();
        this.recruiterName = in.readString();
        this.guid = in.readString();
        this.level = in.readString();
        this.commentTime = in.readLong();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
