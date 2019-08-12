package com.fengniao.fnbluetooth.bluetooth.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;

public class BleUtils {

    private static String EXCUTE_KEY_RESULT = "excute_key_result";

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        android.app.ActivityManager myManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                (ArrayList<android.app.ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            MLog.e("isServiceRunning name==>" + runningService.get(i).service.getClassName());
            if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    public static void setExcuteKeyResult(Context context, boolean result){
        SharedPreferences mPreferences = context.getSharedPreferences("user_info", Context.MODE_APPEND);
        mPreferences.edit().putBoolean(EXCUTE_KEY_RESULT, result).commit();
    }

    public static boolean getExcuteKeyResult(Context context){
        SharedPreferences mPreferences = context.getSharedPreferences("user_info", Context.MODE_APPEND);
        return mPreferences.getBoolean(EXCUTE_KEY_RESULT,false);
    }
}
