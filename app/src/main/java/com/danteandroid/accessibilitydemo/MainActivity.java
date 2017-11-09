package com.danteandroid.accessibilitydemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.likeOrNot)
    Switch likeOrNot;
    @BindView(R.id.random)
    Switch random;
    @BindView(R.id.notification)
    Switch notification;
    @BindView(R.id.tantanState)
    TextView tantanState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();

        if (!Utils.isAccessibilityOpen()) {
            Toast.makeText(this, "请先在辅助功能里打开探探助手~", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goAccessibility();
                }
            }, 500);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyAccessibilityService.stopService = false;
        if (SpUtil.getBoolean(Constants.CREATE_NOTIFICATION, true)) {
            Log.d(TAG, "onStart: createNotification");
            NotificationUtil.createNotification();
        }
    }

    private void init() {
        likeOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.save(Constants.LIKE_OR_NOT, b);
            }
        });
        random.setChecked(SpUtil.getBoolean(Constants.RANDOM));
        random.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.save(Constants.RANDOM, b);
            }
        });
        notification.setChecked(SpUtil.getBoolean(Constants.CREATE_NOTIFICATION));
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.save(Constants.CREATE_NOTIFICATION, b);
                if (b) {
                    NotificationUtil.createNotification();
                } else {
                    NotificationUtil.removeNotification();
                }

            }
        });
        tantanState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isTantanInstalled()) {
                    Utils.goTantan();
                } else {
                    Utils.goMarket(MainActivity.this);
                }
            }
        });
        if (Utils.isTantanInstalled()) {
            tantanState.setText("探探已安裝（点击打开）");
        } else {
            tantanState.setText("探探未安裝（点击下载）");
        }
    }

    public void onClick(View view) {
        goAccessibility();
    }

    private void goAccessibility() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                showDialog();
                break;
            case R.id.donate:
                Utils.donate(MainActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        new AlertDialog.Builder(this).setTitle("关于")
                .setMessage("本版本对应 探探v2.8.5.2，其他版本可能不适用。如有任何建议或问题，请通过支付宝留言反馈")
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}
