package com.pwc.sdc.recruit.data.model;

import android.os.Parcel;
import android.text.TextUtils;

/**
 * @author:dongpo 创建时间: 7/15/2016
 * 描述:
 * 修改:
 */
public class Recruiter extends RecruiterCandidate {
    public Comment comment;
    public boolean isInterview;

    public boolean isCommented(){
        return isInterview;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Recruiter)) {
            return false;
        }

        return TextUtils.equals(guid, ((Recruiter) o).guid);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.comment, flags);
        dest.writeByte(this.isInterview ? (byte) 1 : (byte) 0);
    }

    public Recruiter() {
    }

    protected Recruiter(Parcel in) {
        super(in);
        this.comment = in.readParcelable(Comment.class.getClassLoader());
        this.isInterview = in.readByte() != 0;
    }

    public static final Creator<Recruiter> CREATOR = new Creator<Recruiter>() {
        @Override
        public Recruiter createFromParcel(Parcel source) {
            return new Recruiter(source);
        }

        @Override
        public Recruiter[] newArray(int size) {
            return new Recruiter[size];
        }
    };
}
