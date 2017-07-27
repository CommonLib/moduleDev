package com.pwc.sdc.recruit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author:burt.yang create: 10/13/2016
 * description:
 * revise:
 */
public class CircleProgress extends View {

    private Paint paint;

    /**
     * 旋转间隔
     */
    private static final long ROTATION_DURATION = 2000;

    /**
     * 圆弧最小角度
     */
    private static final int SHORT_SWEEP_ARC = 30;
    /**
     * 圆度最大角度
     */
    private static final int LONG_SWEEP_ARC = 270;

    /**
     * 圆弧每一个状态对应的间隔时间
     */
    private static final long SHORT_SOLID_DURATION = 500;
    private static final long INCREASE_DURATION = 1000;
    private static final long LONG_SOLID_DURATION = 1000;
    private static final long DECREASE_SOLID_DURATION = 1000;

    /**
     * 圆弧每一个状态
     */
    private static final int STATE_SHORT_SOLID = 1;
    private static final int STATE_INCREASE = 2;
    private static final int STATE_LONG_SOLID = 3;
    private static final int STATE_DECREASE_SOLID = 4;

    //动画过程中实时绘制的角度
    private int sweepArc = SHORT_SWEEP_ARC;
    private int startArc = 270;
    private int state = STATE_SHORT_SOLID;
    private long stateTime = 0;
    private int endArc = 0;

    //圆所在的4边距离->确定进度条的半径
    private int left = 100;
    private int right = 500;
    private int top = 100;
    private int bottom = 500;

    /**
     * 1秒绘制30张图
     */
    private static final int ANIMATION_FRAME = 30;
    private RectF mRectF;
    private long mLastDrawTimeMillis;
    private long mNewDrawTimeMillis;
    //1.一直旋转
    //2.进度条长度会发生变化，分为4个阶段
    //     1.最小长度阶段  15
    //     2.长度变长阶段
    //     3.长度维持阶段
    //     4.长度变短阶段
    //     ...
    //     1.最小长度阶段
    private boolean isFirst = true;
    private long mStartMill;
    private long mConstantMill;
    private int count;

    public CircleProgress(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);                    //设置画笔颜色
        paint.setStrokeWidth((float) 30.0);              //线宽
        paint.setStyle(Paint.Style.STROKE);
        mRectF = new RectF();
        mRectF.left = left;
        mRectF.bottom = bottom;
        mRectF.top = top;
        mRectF.right = right;
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            mStartMill = System.currentTimeMillis();
            isFirst = false;
        }
        mConstantMill = System.currentTimeMillis() - mStartMill;
        if (mConstantMill > 10000) {
            Log.e("Log_text", "CircleProgress+onDraw + count" + count);
            count = 0;
            isFirst = true;
        }
        count++;
        canvas.drawColor(Color.WHITE);
        switch (state) {
            case STATE_SHORT_SOLID:
                //短的状态和长的状态起点和终点不发生变化
                if (sweepArc != SHORT_SWEEP_ARC) {
                    sweepArc = SHORT_SWEEP_ARC;
                }
                break;
            case STATE_LONG_SOLID:
                //短的状态和长的状态起点和终点不发生变化
                if (sweepArc != LONG_SWEEP_ARC) {
                    sweepArc = LONG_SWEEP_ARC;
                }
                break;
            case STATE_INCREASE:
                //[30,270] 在duration范围内增加到270
                sweepArc = (int) (((stateTime * 1.0f) / SHORT_SOLID_DURATION) * 240 + 30);
                if (sweepArc > 270) {
                    sweepArc = 270;
                }
                if (sweepArc < 30) {
                    sweepArc = 30;
                }
                break;
            case STATE_DECREASE_SOLID:
                //[270,30]
                sweepArc = (int) ((1 - ((stateTime * 1.0f) / SHORT_SOLID_DURATION)) * 240 + 30);
                if (sweepArc > 270) {
                    sweepArc = 270;
                }
                if (sweepArc < 30) {
                    sweepArc = 30;
                }
                startArc = endArc - sweepArc;
                break;
        }

        if (startArc >= 360 && endArc >= 360) {
            startArc -= 360;
            endArc -= 360;
        }
        //旋转动画
        startArc += 5;
        endArc = startArc + sweepArc;
        Log.d("Log_text", "CircleProgress+onDraw+endArc" + endArc);
        Log.d("Log_text", "CircleProgress+onDraw+startArc" + startArc);
        Log.d("Log_text", "CircleProgress+onDraw+sweepArc" + sweepArc);
        Log.d("Log_text", "----------------------------------------------------------");
        canvas.drawArc(mRectF, startArc, sweepArc, false, paint);
        invalidate();

        mNewDrawTimeMillis = System.currentTimeMillis();
        if (stateTime >= SHORT_SOLID_DURATION) {
            state = toNextState(state);
            stateTime = 0;
        }
        if (mLastDrawTimeMillis != 0) {
            long interval = mNewDrawTimeMillis - mLastDrawTimeMillis;
            stateTime += interval;
        }
        mLastDrawTimeMillis = mNewDrawTimeMillis;
    }

    private boolean isStateTimeOutOfDuration(int stateTime, int state) {
        return false;
    }

    private int toNextState(int state) {
        int nextState = -1;
        switch (state) {
            case STATE_SHORT_SOLID:
                nextState = STATE_INCREASE;
                break;
            case STATE_INCREASE:
                nextState = STATE_LONG_SOLID;
                break;
            case STATE_LONG_SOLID:
                nextState = STATE_DECREASE_SOLID;
                break;
            case STATE_DECREASE_SOLID:
                nextState = STATE_SHORT_SOLID;
                break;
        }
        return nextState;
    }
}
