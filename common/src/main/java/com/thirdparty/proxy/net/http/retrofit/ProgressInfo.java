package com.thirdparty.proxy.net.http.retrofit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author:dongpo 创建时间: 8/2/2016
 * 描述:
 * 修改:
 */
public class ProgressInfo implements Parcelable {
    public long totalSize;
    public long current;
    public boolean isDone;
    public long networkSpeed;
    public String tag;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.totalSize);
        dest.writeLong(this.current);
        dest.writeByte(this.isDone ? (byte) 1 : (byte) 0);
        dest.writeString(this.tag);
    }

    public ProgressInfo() {
    }

    protected ProgressInfo(Parcel in) {
        this.totalSize = in.readLong();
        this.current = in.readLong();
        this.isDone = in.readByte() != 0;
        this.tag = in.readString();
    }

    public static final Creator<ProgressInfo> CREATOR = new Creator<ProgressInfo>() {
        @Override
        public ProgressInfo createFromParcel(Parcel source) {
            return new ProgressInfo(source);
        }

        @Override
        public ProgressInfo[] newArray(int size) {
            return new ProgressInfo[size];
        }
    };
}
