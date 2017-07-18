package com.u3.screenlockertest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Home extends Activity {
    HomeChoice homeChoice; //HomeChoice为设置和启动主屏幕类
    private SharedPreferences sharedPreferences;
    private boolean isLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeChoice = new HomeChoice(this);
        sharedPreferences = getSharedPreferences("homeChoice", MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isLock = sharedPreferences.getBoolean("IsLocked", false);
        //判断锁屏Activity是否在前台
        if (isLock) {
            Intent mIntent = new Intent(this, ScreenLockActivity.class);
            startActivity(mIntent);
            finish();
        }
        //不在前台则启动预设的主屏
        else {

            homeChoice.chooseBackHome();//还没有预设，让用户预设
        }
    }

    public class HomeChoice {
        Context context;
        Intent intent;
        SharedPreferences sharedPreferences;
        Editor editor;
        List<String> pkgNames, actNames;

        public HomeChoice(Context context) {
            this.context = context;
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            sharedPreferences = context.getSharedPreferences("homeChoice", MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        public void chooseBackHome() {

            List<String> pkgNames = new ArrayList<String>();
            List<String> actNames = new ArrayList<String>();
            //获取所有能作为主屏的应用信息
            List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            //将这些应用的包名和Activity名称存入list
            for (int i = 0; i < resolveInfos.size(); i++) {
                String string = resolveInfos.get(i).activityInfo.packageName;
                if (!string.equals(context.getPackageName())) {
                    pkgNames.add(string);
                    string = resolveInfos.get(i).activityInfo.name;
                    actNames.add(string);
                }
            }
            //转化报名为数组
            String[] names = new String[pkgNames.size()];
            for (int i = 0; i < names.length; i++) {
                names[i] = pkgNames.get(i);
            }
            this.pkgNames = pkgNames;
            this.actNames = actNames;

            originalHome(this.pkgNames.get(0), this.actNames.get(0));
        }

        //启动预设主屏
        public void originalHome(String packageName, String activityName) {

            ComponentName componentName = new ComponentName(packageName, activityName);
            Intent intent = new Intent();
            intent.setComponent(componentName);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }


}
