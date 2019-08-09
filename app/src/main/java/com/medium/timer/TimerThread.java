package com.medium.timer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.view.SurfaceHolder;

public class TimerThread extends Thread {

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;

    private boolean mRun;
    private boolean mCounting;
    private long mCount;
    private long mOldCount;
    private long mStartingNanoTime;

    private Bitmap mBitmap;
    private Paint mCoverPaint;
    private RectF mCoverRectF;
    private OnTimerEndListener mOnTimerEndListener;

    public TimerThread(SurfaceHolder holder, Context context) {
        mContext = context;
        mSurfaceHolder = holder;
        mOldCount = 0;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.timer, options);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, TimerView.TIMER_WIDTH, TimerView.TIMER_HEIGHT, false);

        mCoverPaint = new Paint();
        mCoverPaint.setColor(Color.BLACK);
        mCoverRectF = new RectF(35, 104, 342, 412);
    }

    public void setRunning(boolean run) {
        mRun = run;
    }

    public void setCounting(boolean counting) {
        if (counting) {
            mStartingNanoTime = System.nanoTime();
            mCount = 0;
        } else {
            mOldCount = mCount;
        }
        mCounting = counting;
    }

    public void reset() {
        mCount = 0;
        mOldCount = 0;
    }

    public void setOnTimerEndListener(OnTimerEndListener timerEndListener) {
        mOnTimerEndListener = timerEndListener;
    }

    @Override
    public void run() {
        while (mRun) {
            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    doDraw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    private void doDraw(Canvas canvas) {
        if (canvas == null) return;

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        long currentNanoTime = System.nanoTime();
        long diff = (currentNanoTime - mStartingNanoTime) / 1000000000;

        if (mCounting) {
            mCount = diff + mOldCount;
        }

        canvas.drawArc(mCoverRectF, 270, mCount * 6f, true, mCoverPaint);

        if (mCount > 60) {
            setCounting(false);
            if (mOnTimerEndListener != null) {
                mOnTimerEndListener.onTimerEnd();
            }
        }
    }

}
