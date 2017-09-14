package com.pwc.sdc.recruit.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ryin017 on 5/31/17.
 */

public class SignatureView extends View {

    private Paint mPaint;
    private Path mPath;

    private float mPreX, mPreY;
    private boolean mIsGenerateSinature;


    public SignatureView(Context context) {
        super(context);
        init();
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);

        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!mIsGenerateSinature){
            canvas.drawColor(Color.GRAY);
        }
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = event.getX();
                mPreY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    public Bitmap getSignaturePic() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        mIsGenerateSinature = true;
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        draw(canvas);
        mIsGenerateSinature = false;
        return bitmap;
    }

    public boolean isEmpty(){
        return mPath.isEmpty();
    }

    public void cleanSinature(){
        if(!mPath.isEmpty()){
            mPath.reset();
            invalidate();
        }
    }
}