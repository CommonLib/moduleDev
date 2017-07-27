package com.pwc.sdc.recruit.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @author:dongpo 创建时间: 9/1/2016
 * 描述: 具有请求网络功能
 * 修改:
 */
public class PwcImageView extends SimpleDraweeView {
    public PwcImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadImage(Uri imageUri){
        setImageURI(imageUri);
    }
}
