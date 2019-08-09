package com.medium.timer;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.accessibility.AccessibilityNodeInfo;

public class TimerView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int TIMER_WIDTH = 377;
    public static final int TIMER_HEIGHT = 447;

    private TimerThread mTimerThread;

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.setFixedSize(TIMER_WIDTH, TIMER_HEIGHT);

        mTimerThread = new TimerThread(holder, context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mTimerThread.setRunning(true);
        mTimerThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mTimerThread.setRunning(false);

        while (retry) {
            try {
                mTimerThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    public void startCounting() {
        mTimerThread.setCounting(true);
    }

    public void stopCounting() {
        mTimerThread.setCounting(false);
    }

    public void reset() {
        mTimerThread.reset();
    }

    public void setOnTimerEndListener(OnTimerEndListener timerEndListener) {
        mTimerThread.setOnTimerEndListener(timerEndListener);
    }
}
