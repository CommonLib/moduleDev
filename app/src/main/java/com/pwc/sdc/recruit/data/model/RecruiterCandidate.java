package com.pwc.sdc.recruit.data.model;

import android.os.Parcel;

/**
 * @author:dongpo 创建时间: 7/22/2016
 * 描述:
 * 修改:
 */
public class RecruiterCandidate extends ParcelEntity {
    public String department;
    public String name;
    public String level;
    public String guid;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.department);
        dest.writeString(this.name);
        dest.writeString(this.level);
        dest.writeString(this.guid);
    }

    public RecruiterCandidate() {
    }

    protected RecruiterCandidate(Parcel in) {
        this.department = in.readString();
        this.name = in.readString();
        this.level = in.readString();
        this.guid = in.readString();
    }

}
