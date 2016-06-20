/*
 * Copyright (C) 2016 Federico Paolinelli.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.whiterabbit.androidtimersample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private enum TimerState {
        STOPPED,
        RUNNING
    }

    private static final long TIMER_LENGHT = 10; // Ten seconds
    private long mTimeToGo;
    private CountDownTimer mCountDownTimer;
    private TimerState mState;

    @BindView(R.id.main_timer)
    TextView mTimerText;

    @BindView(R.id.main_timer_button)
    Button mTimerButton;

    PrefUtils mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Button b1 = (Button) findViewById(R.id.main_timer_button);
        mPreferences = new PrefUtils(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
        removeAlarm();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mState == TimerState.RUNNING) {
            mCountDownTimer.cancel();
            setAlarm();
        }
    }

    private long getNow() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.getTimeInMillis() / 1000;
    }

    private void initTimer() {
        long startTime = mPreferences.getStartedTime();
        if (startTime > 0) {
            mTimeToGo = (TIMER_LENGHT - (getNow() - startTime));
            if (mTimeToGo <= 0) { // TIMER EXPIRED
                mTimeToGo = TIMER_LENGHT;
                mState = TimerState.STOPPED;
                onTimerFinish();
            } else {
                startTimer();
                mState = TimerState.RUNNING;
            }
        } else {
            mTimeToGo = TIMER_LENGHT;
            mState = TimerState.STOPPED;
        }
        updateTimeUi();
    }

    private void onTimerFinish() {
        Toast.makeText(this, R.string.timer_finished, Toast.LENGTH_SHORT).show();
        mPreferences.setStartedTime(0);
        mTimeToGo = TIMER_LENGHT;
        updateTimeUi();
    }

    private void updateTimeUi() {
        if (mState == TimerState.RUNNING) {
            mTimerButton.setEnabled(false);
        } else {
            mTimerButton.setEnabled(true);
        }

        mTimerText.setText(String.valueOf(mTimeToGo));
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeToGo * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeToGo -= 1;
                updateTimeUi();
            }
            public void onFinish() {
                mState = TimerState.STOPPED;
                onTimerFinish();
                updateTimeUi();
            }
        }.start();
    }


    @OnClick(R.id.main_timer_button)
    public void onButtonClicked() {
        if  (mState == TimerState.STOPPED) {
            mPreferences.setStartedTime(getNow());
            startTimer();
            mState = TimerState.RUNNING;
        }
    }

    public void setAlarm() {
        long wakeUpTime = (mPreferences.getStartedTime() + TIMER_LENGHT) * 1000;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimerExpiredReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            am.setAlarmClock(new AlarmManager.AlarmClockInfo(wakeUpTime, sender), sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, wakeUpTime, sender);
        }
    }

    public void removeAlarm() {
        Intent intent = new Intent(this, TimerExpiredReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

}
