package com.fengniao.fnbluetooth.bluetooth.tryagainHandler;

import android.util.Log;

import com.fengniao.fnbluetooth.bluetooth.FNPageConstant;
import com.fengniao.fnbluetooth.bluetooth.adapter.BleResultData;
import com.fengniao.fnbluetooth.bluetooth.impl.BleResultCallback;
import com.fengniao.fnbluetooth.bluetooth.utils.NetContract;


/**
 * Created by kim on 04/04/2018.
 */

public class GuardRunable implements Runnable {

    //超时时间
    private static int timeout;

    //守护类型
    private int guardType; // 1.蓝牙连接； 2.指令执行


    private static boolean flag = true;

    private BleResultCallback bleResultCallback;
    public GuardRunable(){

    }

    public void setBleResultCallback(BleResultCallback bleResultCallback){
        this.bleResultCallback = bleResultCallback;
    }


    @Override
    public void run() {
        Log.e(FNPageConstant.TAG_BLUETOOTH, "GuardRunable, flag：" + flag + "  timeout:" + timeout);
        while (flag) {
            if (timeout <= 0) {
                //发出通知，做超时处理
                if (guardType == NetContract.GuardType.connectTimeOut){
                    Log.e(FNPageConstant.TAG_BLUETOOTH, "GuardRunable, timeout 蓝牙连接超时");
//                    FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                            "蓝牙执行状态", "连接超时", false);
                    if (bleResultCallback != null){
                        bleResultCallback.onBleConnect(false);
                    }
//                    EventBus.getDefault().post(new EventBlueConnect(false));
                }else {
                    Log.e(FNPageConstant.TAG_BLUETOOTH, "GuardRunable, timeout 指令执行超时");
//                    FileUtils.whiteBLELog(String.valueOf(User.get().getCurOrderDetail().getOrder_info().getOrder_id()),
//                            "蓝牙执行状态", "执行超时", false);
                    if (bleResultCallback != null){
                        bleResultCallback.onBleCommands(new BleResultData(false, false));
                    }
//                    EventBus.getDefault().post(new EventBlueCommand(false));
                }

                flag = false;
            }

            try {
                Thread.sleep(1000);
                timeout--;
                Log.e(FNPageConstant.TAG_BLUETOOTH, "GuardRunable, timeout" + timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setGuardType(int type){
        this.guardType = type;
    }

    public void on_off_GuardRunable(boolean isOn) {
        this.flag = isOn;
        Log.e(FNPageConstant.TAG_BLUETOOTH, "GuardRunable, on_off_GuardRunable " + isOn);
    }


}
