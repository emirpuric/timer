package com.medium.timer;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private TimerView mTimerView;
    private Button mStart, mStop, mReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerView = findViewById(R.id.timer);
        mTimerView.setOnTimerEndListener(new OnTimerEndListener() {
            @Override
            public void onTimerEnd() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Timer")
                                    .setMessage("Counting finished!")
                                    .setCancelable(true)
                                    .show();
                        }
                    }
                });
            }
        });

        mStart = findViewById(R.id.button_start);
        mStop = findViewById(R.id.button_stop);
        mReset = findViewById(R.id.button_reset);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onStartClick(View view) {
        mTimerView.startCounting();
        mStart.setEnabled(false);
        mStop.setEnabled(true);
        mReset.setEnabled(false);
    }

    public void onStopClick(View view) {
        mTimerView.stopCounting();
        mStart.setEnabled(true);
        mStop.setEnabled(false);
        mReset.setEnabled(true);
    }

    public void onResetClick(View view) {
        mTimerView.reset();
        mStart.setEnabled(true);
        mStop.setEnabled(false);
        mReset.setEnabled(false);
    }
}
