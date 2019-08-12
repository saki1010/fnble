package com.fengniao.fnbluetooth.bluetooth.impl;


import com.fengniao.fnbluetooth.bluetooth.adapter.BleResultData;

public interface BleResultCallback {

    /**
     * 用户拒绝打开蓝牙
     */
    void onUserReject();

    /**
     *  蓝牙返回结果，是否连接
     * @param isConnect
     */
    void onBleConnect(boolean isConnect);

    /**
     *  蓝牙返回结果，执行
     * @param bleData
     */
    void onBleCommands(BleResultData bleData);

}
