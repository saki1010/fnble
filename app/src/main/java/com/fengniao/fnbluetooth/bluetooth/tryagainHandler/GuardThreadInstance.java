package com.fengniao.fnbluetooth.bluetooth.tryagainHandler;


import android.util.Log;

import com.fengniao.fnbluetooth.bluetooth.FNPageConstant;


/**
 * Created by kim on 15/03/2018.
 */

public class GuardThreadInstance {

    /**
     * 设置守护线程
     * 监听 5秒连不上盒子，返回处理
     */
     TryAgainThread disconnectedThread;

    public  TryAgainThread getDisconnectedInstance() {
        if (disconnectedThread == null) {
            disconnectedThread = new TryAgainThread();
        }
        disconnectedThread.setTimeout(5);
        return disconnectedThread;
    }

    public  void destoryDisconnected() {
        Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, stopThread");
        if (disconnectedThread != null) {
            disconnectedThread.stopThread();
            disconnectedThread = null;
        }
    }

    public void startDisconnected(){
        Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, startThread");
        getDisconnectedInstance();
        if (disconnectedThread != null){
            Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, startThread new");
            disconnectedThread.startThread();
        }
    }

    public  void resetDisconnected() {
        Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, reset");
        if (disconnectedThread != null){
            disconnectedThread.setTimeout(5);
        }
    }

}
