package com.u3.screenlockertest;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ScreenLockActivity extends Activity {
    private Button unlockButton;
    private SharedPreferences sharedPreferences;
    private Editor editor;
    private boolean isFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//解除系统锁屏
        setContentView(R.layout.main_layout);
        setView();//设置关闭按钮
        sharedPreferences = getSharedPreferences("homeChoice", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setView() {
        unlockButton = (Button) findViewById(R.id.bt_open);
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("IsLocked", !isFront);
                editor.commit();
            }
        });
    }

    //屏蔽back
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //屏蔽recentApp按钮
    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = sharedPreferences.getBoolean("IsLocked",false);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
