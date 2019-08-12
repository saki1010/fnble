package com.fengniao.fnbluetooth.bluetooth.tryagainHandler;

/**
 * Created by kim on 13/03/2018.
 */

import android.util.Log;

import com.fengniao.fnbluetooth.bluetooth.FNPageConstant;
import com.fengniao.fnbluetooth.bluetooth.impl.ICommandCallbackListener;


/**
 * 该线程运行3s
 * 3s内蓝牙有反馈信息，则提前结束线程，根据蓝牙返回做成功或失败处理
 * 3s内蓝牙没有反馈信息，则结束线程，做失败处理
 */
public class TryAgainThread extends Thread {

    private int timeout;

    //重发次数，目前只做重发一次处理

    ICommandCallbackListener iCommandCallbackListener;


    private boolean flag = true;

    public void setiCommandCallbackListener(ICommandCallbackListener iCommandCallbackListener) {
        if (this.iCommandCallbackListener != null) {
            this.iCommandCallbackListener = null;
        }
        this.iCommandCallbackListener = iCommandCallbackListener;
    }


    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public void stopThread() {
        timeout = 0;
        iCommandCallbackListener = null;
    }


    public void startThread() {
        if (this != null) {
            this.start();
        }
    }


    @Override
    public void run() {

        while (flag) {
            if (timeout <= 0){
                //发出通知，做超时处理
                Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, timeout 超时：" + timeout);
                Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, timeout 超时：" + iCommandCallbackListener);
                if (iCommandCallbackListener != null)
                    iCommandCallbackListener.commandCallback(false);
               flag = false;
            }

            try {
                Thread.sleep(1000);
                timeout--;
                Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, timeout" + timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }





//        long st = resetTime();
//        while (flag) {
//            long current = System.currentTimeMillis();
//            if (current - st > timeout) {
//                //发出通知，做超时处理
//                Log.e(FNPageConstant.TAG_BLUETOOTH, "DisconnectednHolder, timeout");
//                if (iCommandCallbackListener != null)
//                    iCommandCallbackListener.commandCallback(false);
//                //3s结束线程
//                flag = false;
//            }
//        }

    }




}
