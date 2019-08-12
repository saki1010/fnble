package com.fengniao.fnbluetooth.bluetooth.tryagainHandler;

import android.util.Log;


import com.fengniao.fnbluetooth.bluetooth.FNPageConstant;
import com.fengniao.fnbluetooth.bluetooth.impl.BleResultCallback;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by kim on 03/04/2018.
 */

public class GuardThread {

    ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();

    //守护线程
    GuardRunable guardRunable;

    public void startThread(int type, int timeout, BleResultCallback bleResultCallback) {
        if (guardRunable == null) {
            guardRunable = new GuardRunable();
        }
        //每次有新的回调都传进去，防止引用到旧的
        guardRunable.setBleResultCallback(bleResultCallback);
        Log.e(FNPageConstant.TAG_BLUETOOTH, "GuardRunable startThread：" + guardRunable.hashCode());
        guardRunable.setTimeout(timeout);
        guardRunable.setGuardType(type);
        guardRunable.on_off_GuardRunable(true);

        scheduledThreadPool.execute(guardRunable);
    }



    public void destoryGuardThread() {
        if (guardRunable != null){
            Log.e(FNPageConstant.TAG_BLUETOOTH, "GuardRunable destoryGuardThread：" + guardRunable.hashCode());
            guardRunable.on_off_GuardRunable(false);
        }

    }





}
