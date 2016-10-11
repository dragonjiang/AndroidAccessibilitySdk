package com.example.nd99u;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.demo.dragonjiang.accessilibility_sdk.core.command.CmdExecutor;
import com.demo.dragonjiang.accessilibility_sdk.core.command.shellCmd.ShellHomeCmd;
import com.demo.dragonjiang.accessilibility_sdk.utils.LaunchUtil;
import com.demo.dragonjiang.accessilibility_sdk.utils.SPUtils;
import com.demo.dragonjiang.accessilibility_sdk.utils.TimeUtil;
import com.example.nd99u.utils.log.utils.LogUtils;

import java.util.Calendar;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog
        .OnTimeSetListener {

    private static final String TAG = "MainActivity";

    private Button mBtnSetting;
    private Button mBtnStart;
    private Button mBtnStop;
    private Button mBtnLaunchTime;
    private Button mBtnTestLaunch;
    private TextView mTvLaunchTime;
    private EditText mEtName;

    private long mLaunchTime;
    private String mStrLaunchTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnSetting = (Button) findViewById(R.id.btn_setting);
        mBtnTestLaunch = (Button) findViewById(R.id.btn_test_launch);
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnLaunchTime = (Button) findViewById(R.id.btn_launch_time);
        mTvLaunchTime = (TextView) findViewById(R.id.tv_launch_time);
        mEtName = (EditText) findViewById(R.id.et_name);

        mBtnSetting.setOnClickListener(this);
        mBtnTestLaunch.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnLaunchTime.setOnClickListener(this);

        mStrLaunchTime = (String) SPUtils.get(this, Constants.SP_KEY.LAUNCH_TIME, "08:51");
        mLaunchTime = TimeUtil.parseTime(mStrLaunchTime);
        mTvLaunchTime.setText(mStrLaunchTime);
        mEtName.setText((String) SPUtils.get(this, Constants.SP_KEY.NAME, "江龙强"));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_setting) {
            Intent i = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(i);

        } else if (id == R.id.btn_test_launch) {
            SPUtils.put(this, Constants.SP_KEY.RESULT, Constants.RUN_RESULT.NONE);
            saveNames();
            saveLaunchTime();
            LaunchUtil.luanchApp(Constants.APP_PKG, MainActivity.this);

        } else if (id == R.id.btn_start) {

            saveNames();
            saveLaunchTime();

            NotificationService.startService(MainActivity.this);

            CmdExecutor.executeCancelable(new ShellHomeCmd(), 1000, false)
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {
                            LogUtils.FLLog(TAG, "钉钉签到程序将在后台持续为您服务");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Integer integer) {

                        }
                    });

        } else if (id == R.id.btn_stop) {
            Intent stopIntent = new Intent(MainActivity.this, NotificationService.class);
            stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND);
            startService(stopIntent);

        } else if (id == R.id.btn_launch_time) {
            TimePickerDialog dlg = new TimePickerDialog(this, this, 0, 0, true);
            int[] hm = getHourMinute(mLaunchTime);
            dlg.updateTime(hm[0], hm[1]);
            dlg.show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        LogUtils.FLLog(TAG, "hour:" + hourOfDay + "   minute:" + minute);

        String time = buildTimeString(hourOfDay, minute);

        mStrLaunchTime = time;
        mLaunchTime = TimeUtil.parseTime(time);
        mTvLaunchTime.setText(time);
    }


    /**
     * 0 hour  1 minute
     *
     * @return
     */
    private String buildTimeString(int hour, int minute) {
        StringBuilder sb = new StringBuilder();
        if (hour < 10) {
            sb.append("0");
            sb.append(hour);
        } else {
            sb.append(hour);
        }

        sb.append(":");

        if (minute < 10) {
            sb.append("0");
            sb.append(minute);
        } else {
            sb.append(minute);
        }

        return sb.toString();
    }

    private int[] getHourMinute(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int[] hm = new int[2];
        hm[0] = c.get(Calendar.HOUR_OF_DAY);
        hm[1] = c.get(Calendar.MINUTE);
        return hm;
    }

    private void saveLaunchTime() {
        SPUtils.put(this, Constants.SP_KEY.LAUNCH_TIME, mStrLaunchTime);
    }

    private void saveNames() {
        SPUtils.put(this, Constants.SP_KEY.NAME, mEtName.getText().toString());
    }
}
