package com.fengniao.fnbluetooth.bluetooth.adapter;

import android.bluetooth.BluetoothAdapter;

import com.fengniao.fnbluetooth.bluetooth.JDY_type;
import com.fengniao.fnbluetooth.bluetooth.impl.CallbackListenter;


/**
 * Created by kim on 08/03/2018.
 */

public interface IBluetoothAdapter {

    BluetoothAdapter getBluetoothAdapter();

    /**
     * 查找蓝牙设备
     * @param enable true 开始查找；false 停止查找
     */
    void enable_scan(boolean enable);

    void setScanEnable();


    JDY_type dv_type(byte[] p);


    void addCallbackListenter(CallbackListenter callbackListenter);


    void removeCallbackListener();


    /**
     * 传入当前车辆蓝牙mac地址
     * @param address
     */
    void setCarMacAddress(String address);

}
